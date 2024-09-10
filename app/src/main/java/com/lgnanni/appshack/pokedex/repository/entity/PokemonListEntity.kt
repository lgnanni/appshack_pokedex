package com.lgnanni.appshack.pokedex.repository.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemonList")
data class PokemonListEntity (
    @PrimaryKey val name: String,
    val url: String,
    var starred: Boolean
    )