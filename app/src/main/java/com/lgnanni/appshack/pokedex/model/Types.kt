package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Types(
    val slot: Int,
    val type: PokemonType,
) : Parcelable
