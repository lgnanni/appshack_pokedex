package com.lgnanni.appshack.pokedex.repository

import android.content.Context
import com.lgnanni.appshack.pokedex.model.PokemonListItem
import com.lgnanni.appshack.pokedex.network.ApiService
import com.lgnanni.appshack.pokedex.repository.dao.PokemonListDao
import com.lgnanni.appshack.pokedex.repository.entity.PokemonListEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

interface PokedexRepo {

    fun getPokemonList(): Flow<List<PokemonListItem>>
}

@Singleton
class PokedexRepoImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val pokemonApi: ApiService,
    private val pokemonDao : PokemonListDao) : PokedexRepo {


    override fun getPokemonList(): Flow<List<PokemonListItem>> = flow {
        // Emit cached data first
        val cachedUsers = pokemonDao.getPokemons().map { it.toPokemonListItem() }
        emit(cachedUsers)

        // Fetch data from remote, if fails, fetch from local
        try {
            // Fetch from remote
            val remotePokemons = pokemonApi.getPokemons()
            // Cache the result locally
            remotePokemons.body()?.results?.let { pokemonDao.insertPokemons(it.map { it -> it.toPokemonListEntity() }) }
            // Emit fresh data
            remotePokemons.body()?.results?.let { emit(it) }
        } catch (e: Exception) {
            // Handle exception
            e.printStackTrace()
        }
    }
}


// Extension functions to convert between User and UserEntity
fun PokemonListEntity.toPokemonListItem() = PokemonListItem(name, url)
fun PokemonListItem.toPokemonListEntity() = PokemonListEntity(name, url)