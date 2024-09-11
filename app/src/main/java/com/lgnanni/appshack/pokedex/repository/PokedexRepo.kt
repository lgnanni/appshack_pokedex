package com.lgnanni.appshack.pokedex.repository

import com.lgnanni.appshack.pokedex.model.Chain
import com.lgnanni.appshack.pokedex.model.EvolutionDetails
import com.lgnanni.appshack.pokedex.model.EvolutionTrigger
import com.lgnanni.appshack.pokedex.model.Pokemon
import com.lgnanni.appshack.pokedex.model.PokemonDetails
import com.lgnanni.appshack.pokedex.model.PokemonList
import com.lgnanni.appshack.pokedex.model.PokemonListItem
import com.lgnanni.appshack.pokedex.model.SpeciesData
import com.lgnanni.appshack.pokedex.model.SpeciesInfo
import com.lgnanni.appshack.pokedex.model.Types
import com.lgnanni.appshack.pokedex.network.ApiService
import com.lgnanni.appshack.pokedex.network.RetrofitModule
import com.lgnanni.appshack.pokedex.repository.dao.PokemonDetailsDao
import com.lgnanni.appshack.pokedex.repository.dao.PokemonListDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

interface PokedexRepo {
    fun getPokemonList(): Flow<List<PokemonListItem>>
    fun getPokemonDetails(id: Int): Flow<PokemonDetails>
}


@Singleton
class PokedexRepoImpl @Inject constructor(
    private val pokemonApi: ApiService,
    private val pokemonListDao : PokemonListDao,
    private val pokemonDetailsDao: PokemonDetailsDao) : PokedexRepo {


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getPokemonList(): Flow<List<PokemonListItem>> {
        var cachedCount = 0
        runBlocking {
            // Emit cached data first
            val cachedPokemons = pokemonListDao.getPokemons().map { it.toPokemonListItem() }
            cachedCount = cachedPokemons.size
        }

        return if (cachedCount > 0) {
            emitCachedPokemonList()
        } else
            getFirstPokemonCall().flatMapLatest { pokemonList ->
                getFullPokemonCall(pokemonList.count)
            }
    }

    private fun emitCachedPokemonList() : Flow<List<PokemonListItem>> = flow{
        // Emit cached data first
        val cachedPokemons = pokemonListDao.getPokemons().map { it.toPokemonListItem() }
        emit(cachedPokemons)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getPokemonDetails(id: Int): Flow<PokemonDetails> {

        var pokeName = ""

        runBlocking {
            pokemonDetailsDao.getPokemonDetails(id)?.let {
                it.toPokemonDetails()
                pokeName = it.name
            }

        }

        return if (pokeName.isNotEmpty()) {
            emitCachedPokemonDetails(id)
        } else
            // Fetch from remote
            getPokemonGeneralDetails(id).flatMapLatest { pokemon ->
                getPokemonSpeciesDetails(pokemon.species.url).flatMapLatest { speciesInfo ->
                    getPokemonEvoDetails(speciesInfo.evoChain.url).flatMapLatest { evoChain ->
                        val evoDetails = getPokemonEvolutionToDetails(pokemon.name, evoChain)

                        if (evoDetails.trigger.url.isEmpty()) {
                            getPokemonTypes(pokemon.types).flatMapLatest { typeSprites ->
                                val starred = getPokemonIsStarred(pokemon.name)
                                val speciesData = SpeciesData(evoDetails, EvolutionTrigger(), getPokemonEvolutionTo(pokemon.name, evoChain), speciesInfo.flavorTextEntries)
                                makePokemonDetails(pokemon, speciesData, typeSprites, starred)
                            }
                        } else {
                            getPokemonEvoTrigger(evoDetails.trigger.url).flatMapLatest { evoTrigger ->
                                getPokemonTypes(pokemon.types).flatMapLatest { typeSprites ->
                                    val starred = getPokemonIsStarred(pokemon.name)
                                    val speciesData = SpeciesData(
                                        evoDetails,
                                        evoTrigger,
                                        getPokemonEvolutionTo(pokemon.name, evoChain),
                                        speciesInfo.flavorTextEntries
                                    )
                                    makePokemonDetails(pokemon, speciesData, typeSprites, starred)
                                }
                            }
                        }
                    }
            }
        }
    }

    /**
     * The information about evolutions is quite convoluted and forces this manual approach
     * with nested arrays of the same type until there's an empty array object
     */
    private fun getPokemonEvolutionToDetails(pokemon: String, evoChain: Chain) : EvolutionDetails {
        if (pokemon.contains(evoChain.chain.species.name)) {
            val details = if(evoChain.chain.evolvesTo.isEmpty())
                EvolutionDetails()
            else evoChain.chain.evolvesTo.first().evoDetails.first()
            return details
        }

        val firstPath = evoChain.chain.evolvesTo
        val firstChild = firstPath.find { pokemon.contains(it.species.name)}

        if (firstChild != null) {
            val details = if(firstChild.evolvesTo.isEmpty())
                EvolutionDetails()
            else firstChild.evolvesTo.first().evoDetails.first()
            return details
        }
       else {
            val secondPath = firstPath.first().evolvesTo
        val secondChild = secondPath.find { pokemon.contains(it.species.name) }

            if (secondChild != null) {
                val details = if(secondChild.evolvesTo.isEmpty())
                    EvolutionDetails()
                else secondChild.evolvesTo.first().evoDetails.first()
                return details
            }
        }

        return EvolutionDetails()
    }


    /**
     * The information about evolutions is quite convoluted and forces this manual approach
     * with nested arrays of the same type until there's an empty array object
     */
    private fun getPokemonEvolutionTo(pokemon: String, evoChain: Chain) : String {
        if (pokemon.contains(evoChain.chain.species.name))
            return if (evoChain.chain.evolvesTo.isEmpty()) "" else evoChain.chain.evolvesTo.first().species.name


        val firstChild = evoChain.chain.evolvesTo.first()

        if (pokemon.contains(firstChild.species.name)) {
            val evolveTo = if(firstChild.evolvesTo.isEmpty()) ""
                    else firstChild.evolvesTo.first().species.name

            return evolveTo
        }
        else {
            val secondChild = firstChild.evolvesTo.first()
            if (pokemon.contains(secondChild.species.name)) {
                val evolveTo = if (secondChild.evolvesTo.isEmpty()) ""
                else secondChild.evolvesTo.first().species.name

                return evolveTo
            }
        }

        return ""
    }


    private suspend fun getPokemonIsStarred(name: String) : Boolean {
        return pokemonListDao.getPokemon(name).starred
    }

    private fun emitCachedPokemonDetails(id: Int) : Flow<PokemonDetails> = flow{
        // Emit cached data first
        val cachedPokemonDetails = pokemonDetailsDao.getPokemonDetails(id).toPokemonDetails()
        val starred = getPokemonIsStarred(cachedPokemonDetails.name)

        val starredDetails = PokemonDetails(cachedPokemonDetails, starred)
        emit(starredDetails)
    }

    private fun makePokemonDetails(
        pokemon: Pokemon,
        speciesData: SpeciesData,
        typeSprites: List<String>,
        starred: Boolean): Flow<PokemonDetails> = flow {
        val pokemonDetails = PokemonDetails(
            pokemon.id,
            pokemon.name,
            pokemon.cries,
            pokemon.sprites.other.officialArtwork,
            speciesData,
            typeSprites,
            starred
        )
        // Cache the result locally
        pokemonDetailsDao.insertPokemonDetails(pokemonDetails.toPokemonDetailsEntity())
        // Emit fresh data
        emit(pokemonDetails)
    }

    private fun getPokemonTypes(typesUrl: List<Types>): Flow<List<String>> = flow {
        try {
            val typesSprites: MutableList<String> = emptyList<String>().toMutableList()
            typesUrl.forEach {
                val typeId = it.type.url.substringAfter("type/").removeSuffix("/")
                val remoteType = pokemonApi.getTypeSprite(typeId)
                typesSprites.add(remoteType.body()!!.sprites.generationVIII.swordShield.nameIcon)
            }

            emit(typesSprites)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getPokemonSpeciesDetails(url: String): Flow<SpeciesInfo> = flow {
        try {
            val remotePokemonSpecies = pokemonApi.getSpeciesDetails(RetrofitModule.removeBaseUrl(url))
            emit(remotePokemonSpecies.body()!!)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPokemonEvoDetails(url: String): Flow<Chain> = flow {
        try {
            val remoteEvoChain = pokemonApi.getEvolutionDetails(RetrofitModule.removeBaseUrl(url))
            emit(remoteEvoChain.body()!!)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPokemonEvoTrigger(url: String): Flow<EvolutionTrigger> = flow {
        try {
            val remoteEvoTrigger = pokemonApi.getEvolutionTrigger(RetrofitModule.removeBaseUrl(url))
            emit(remoteEvoTrigger.body()!!)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPokemonGeneralDetails(id: Int): Flow<Pokemon> = flow {
        try {
            val remotePokemonDetails = pokemonApi.getPokemonDetails(id)
            val pokemon = remotePokemonDetails.body()!!
            emit(pokemon)
        }
        catch (e: Exception) {
            // Handle exception
            e.printStackTrace()
        }
    }

    private fun getFirstPokemonCall(): Flow<PokemonList> = flow {
        // Emit cached data first
        try {
            // Fetch from remote
            val remotePokemons = pokemonApi.getPokemons()
            val pokemonList = remotePokemons.body()!!
            emit(pokemonList)
        } catch (e: Exception) {
            // Handle exception
            e.printStackTrace()
        }
    }

    private fun getFullPokemonCall(
        count: Int): Flow<List<PokemonListItem>> = flow {
        // Emit cached data first
        try {
            // Fetch from remote
            val remotePokemons = pokemonApi.getPokemonsOffset(0, count)

            val pokemonList = remotePokemons.body()!!

            //There are variations which are outside the norm on the api
            //recognizabled by their url id being on the 10000s when theres
            // a 4 digits amount of pokemons so we remove them to avoid breaks on random pokemon selection
            val filteredList = pokemonList.results.filter { item -> item.url.substringAfter("pokemon/").length <= 5 }

            pokemonListDao.insertPokemons(filteredList.map { it.toPokemonListEntity() })
            emit(filteredList)
        } catch (e: Exception) {
            // Handle exception
            e.printStackTrace()
        }
    }

    suspend fun updatePokemon(name: String, starred: Boolean) {
        val pokeEntity = pokemonListDao.getPokemon(name)
        pokeEntity.starred = starred

        pokemonListDao.updatePokemonData(pokeEntity)
    }
}

