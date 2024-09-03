package com.lgnanni.appshack.pokedex.repository

import android.content.Context
import com.lgnanni.appshack.pokedex.model.PokemonData
import com.lgnanni.appshack.pokedex.model.PokemonListItem
import com.lgnanni.appshack.pokedex.network.RetrofitInstance
import com.lgnanni.appshack.pokedex.repository.entity.PokemonListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface PokedexRepo {

    fun getData(): Flow<List<PokemonListItem>>
}

class PokedexRepoImpl(private val context: Context) : PokedexRepo {

    private val pokemonApi = RetrofitInstance.api
    private val pokemonDao = DatabaseInstance.getDatabase(context).pokemonListDao()

    override fun getData(): Flow<List<PokemonListItem>> = flow {
        // Emit cached data first
        val cachedUsers = pokemonDao.getPokemons().map { it.toPokemonListItem() }
        emit(cachedUsers)

        // Fetch data from remote, if fails, fetch from local
        try {
            // Fetch from remote
            val remotePokemons = pokemonApi.getPokemons()
            // Cache the result locally
            remotePokemons.body()?.results?.let { pokemonDao.insertPokemons(it.map { it.toPokemonListEntity() }) }
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