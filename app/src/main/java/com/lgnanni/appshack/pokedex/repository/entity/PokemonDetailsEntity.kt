package com.lgnanni.appshack.pokedex.repository.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lgnanni.appshack.pokedex.model.Cries
import com.lgnanni.appshack.pokedex.model.OfficialSprites
import com.lgnanni.appshack.pokedex.model.SpeciesInfo
import com.lgnanni.appshack.pokedex.model.TypeSprite

@Entity(tableName = "pokemonDetails")
data class PokemonDetailsEntity (
    @PrimaryKey val id: Int,
    val name: String,
    val cries: Cries,
    val officialSprite: OfficialSprites,
    val speciesInfo: SpeciesInfo,
    val typeSprite: TypeSprite
)