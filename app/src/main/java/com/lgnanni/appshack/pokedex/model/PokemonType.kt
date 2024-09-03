package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonType(
    val name: String,
    val sprites: TypeSprite
) : Parcelable