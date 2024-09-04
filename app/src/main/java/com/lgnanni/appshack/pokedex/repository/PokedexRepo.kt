package com.lgnanni.appshack.pokedex.repository

import android.content.Context
import com.lgnanni.appshack.pokedex.model.PokemonDetails
import com.lgnanni.appshack.pokedex.model.PokemonListItem
import com.lgnanni.appshack.pokedex.network.ApiService
import com.lgnanni.appshack.pokedex.repository.dao.PokemonDetailsDao
import com.lgnanni.appshack.pokedex.repository.dao.PokemonListDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

interface PokedexRepo {

    fun getPokemonList(nextUrl: String = ""): Flow<List<PokemonListItem>>
    fun getPokemonDetails(id: Int): Flow<PokemonDetails>
}

@Singleton
class PokedexRepoImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val pokemonApi: ApiService,
    private val pokemonListDao : PokemonListDao,
    private val pokemonDetailsDao: PokemonDetailsDao) : PokedexRepo {


    override fun getPokemonList(nextUrl: String): Flow<List<PokemonListItem>> = flow {

        if (nextUrl.isEmpty()) {
            // Emit cached data first
            val cachedPokemons = pokemonListDao.getPokemons().map { it.toPokemonListItem() }
            emit(cachedPokemons)
        }

        // Fetch data from remote, if fails, fetch from local
        try {
            // Fetch from remote
            val remotePokemons = if(nextUrl.isEmpty())
                pokemonApi.getPokemons()
            else
                pokemonApi.getPokemonsOffset(pokemonApi.removeBaseUrl(nextUrl))

            remotePokemons.body()?.count?.let { StoreData(context).setPokemonCount(it) }
            remotePokemons.body()?.next?.let { StoreData(context).setNextPage(it) }

            // Cache the result locally
            remotePokemons.body()?.results?.let { pokemonListDao.insertPokemons(it.map { it -> it.toPokemonListEntity() }) }
            // Emit fresh data
            remotePokemons.body()?.results?.let { emit(it) }
        } catch (e: Exception) {
            // Handle exception
            e.printStackTrace()
        }
    }

    override fun getPokemonDetails(id: Int): Flow<PokemonDetails> = flow {
        // Emit cached data first
        val cachedPokemonDetails = pokemonDetailsDao.getPokemonDetails(id).toPokemonDetails()
        emit(cachedPokemonDetails)
    }
}

