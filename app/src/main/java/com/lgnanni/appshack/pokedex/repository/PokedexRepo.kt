package com.lgnanni.appshack.pokedex.repository

import android.content.Context
import androidx.datastore.dataStore
import com.lgnanni.appshack.pokedex.model.Pokemon
import com.lgnanni.appshack.pokedex.model.PokemonDetails
import com.lgnanni.appshack.pokedex.model.PokemonList
import com.lgnanni.appshack.pokedex.model.PokemonListItem
import com.lgnanni.appshack.pokedex.model.SpeciesInfo
import com.lgnanni.appshack.pokedex.model.Types
import com.lgnanni.appshack.pokedex.network.ApiService
import com.lgnanni.appshack.pokedex.network.RetrofitModule
import com.lgnanni.appshack.pokedex.repository.dao.PokemonDetailsDao
import com.lgnanni.appshack.pokedex.repository.dao.PokemonListDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

import javax.inject.Inject
import javax.inject.Singleton

interface PokedexRepo {
    fun getPokemonList(): Flow<List<PokemonListItem>>
    fun getPokemonDetails(id: Int): Flow<PokemonDetails>
}


@Singleton
class PokedexRepoImpl @Inject constructor(
    @ApplicationContext private val context: Context,
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
            val cachedPokemonDetails = pokemonDetailsDao.getPokemonDetails(id)?.let {
                it.toPokemonDetails()
            } ?: PokemonDetails()
            pokeName = cachedPokemonDetails.name
        }

        return if (pokeName.isNotEmpty()) {
            emitCachedPokemonDetails(id)
        } else
            // Fetch from remote
            getPokemonGeneralDetails(id).flatMapLatest { pokemon ->
                getPokemonSpeciesDetails(pokemon.species.url).flatMapLatest { speciesInfo ->
                    getPokemonTypes(pokemon.types).flatMapLatest { typeSprites ->
                        makePokemonDetails(pokemon, speciesInfo, typeSprites)
                    }
            }
        }

    }

    private fun emitCachedPokemonDetails(id: Int) : Flow<PokemonDetails> = flow{
        // Emit cached data first
        val cachedPokemonDetails = pokemonDetailsDao.getPokemonDetails(id).toPokemonDetails()
        emit(cachedPokemonDetails)
    }

    private fun makePokemonDetails(pokemon: Pokemon, speciesInfo: SpeciesInfo, typeSprites: List<String>): Flow<PokemonDetails> = flow {
        val pokemonDetails = PokemonDetails(
            pokemon.id,
            pokemon.name,
            pokemon.cries,
            pokemon.sprites.other.officialArtwork,
            speciesInfo,
            typeSprites
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
}

