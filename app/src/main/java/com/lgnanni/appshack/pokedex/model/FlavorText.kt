package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FlavorText(
    @SerializedName("flavor_text") val flavorText: String,
    val language: PokemonUrl): Parcelable