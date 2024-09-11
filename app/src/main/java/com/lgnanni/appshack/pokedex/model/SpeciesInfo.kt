package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpeciesInfo(
    @SerializedName("evolution_chain") val evoChain: EvolutionChain,
    @SerializedName("flavor_text_entries") val flavorTextEntries: List<FlavorText>) : Parcelable
