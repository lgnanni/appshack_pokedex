package com.lgnanni.appshack.pokedex.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lgnanni.appshack.pokedex.repository.entity.PokemonDetailsEntity

@Dao
interface PokemonDetailsDao {
    @Query("SELECT * FROM pokemonDetails  WHERE id = :id")
    suspend fun getPokemonDetails(id: Int): PokemonDetailsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetails(pokemon: PokemonDetailsEntity)
}