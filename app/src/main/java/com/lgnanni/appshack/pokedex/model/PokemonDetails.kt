package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonDetails(
    val id: Int,
    val name: String,
    val cries: Cries,
    val officialSprite: OfficialSprites,
    val speciesInfo: SpeciesInfo,
    val typeSprites: List<String>
    ) : Parcelable