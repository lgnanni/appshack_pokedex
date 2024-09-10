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
    val typeSprites: List<String>,
    val starred: Boolean
    ) : Parcelable {

        constructor() : this (
            -1,
            "",
            Cries("", ""),
            OfficialSprites("", ""),
            SpeciesInfo(EvoChain( ""), emptyList()),
            emptyList(),
    false
        )

    constructor(details: PokemonDetails, starred: Boolean) : this (
        details.id,
        details.name,
        details.cries,
        details.officialSprite,
        details.speciesInfo,
        details.typeSprites,
        starred
    )

}

