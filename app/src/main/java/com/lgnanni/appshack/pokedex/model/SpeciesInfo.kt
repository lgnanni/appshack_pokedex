package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpeciesInfo(val evolvesFromSpecies: String?, val flavorTextEntries: List<FlavorText>) : Parcelable