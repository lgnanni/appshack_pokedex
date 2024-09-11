package com.lgnanni.appshack.pokedex.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lgnanni.appshack.pokedex.repository.entity.PokemonListEntity

@Dao
interface PokemonListDao {
    @Query("SELECT * FROM pokemonList")
    suspend fun getPokemons(): List<PokemonListEntity>

    @Query("SELECT * FROM pokemonList where name = :name LIMIT 1")
    suspend fun getPokemon(name: String): PokemonListEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemons(pokemons: List<PokemonListEntity>)

    @Update
    suspend fun updatePokemonData(item: PokemonListEntity)
}