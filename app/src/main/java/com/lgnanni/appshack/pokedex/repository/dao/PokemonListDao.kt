package com.lgnanni.appshack.pokedex.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lgnanni.appshack.pokedex.repository.entity.PokemonListEntity

@Dao
interface PokemonListDao {
    @Query("SELECT * FROM pokemons")
    suspend fun getPokemons(): List<PokemonListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemons(users: List<PokemonListEntity>)
}