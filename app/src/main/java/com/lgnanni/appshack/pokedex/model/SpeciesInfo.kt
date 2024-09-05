package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpeciesInfo(
    @SerializedName("evolves_from_species") val evolvesFromSpecies: EvolvesFrom,
    @SerializedName("flavor_text_entries") val flavorTextEntries: List<FlavorText>) : Parcelable

@Parcelize
data class EvolvesFrom(val name: String?, val url: String) : Parcelable