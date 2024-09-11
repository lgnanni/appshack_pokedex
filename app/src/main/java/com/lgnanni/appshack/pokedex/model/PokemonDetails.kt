package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonDetails(
    val id: Int,
    val name: String,
    val cries: Cries,
    val officialSprite: OfficialSprites,
    val speciesData: SpeciesData,
    val typeSprites: List<String>,
    val starred: Boolean
    ) : Parcelable {

    constructor(details: PokemonDetails, starred: Boolean) : this (
        details.id,
        details.name,
        details.cries,
        details.officialSprite,
        details.speciesData,
        details.typeSprites,
        starred
    )

}

@Parcelize
data class SpeciesData(val evolutionDetails: EvolutionDetails, val evolutionTrigger: EvolutionTrigger, val evolvesTo: String, val flavorTexts: List<FlavorText>) : Parcelable

