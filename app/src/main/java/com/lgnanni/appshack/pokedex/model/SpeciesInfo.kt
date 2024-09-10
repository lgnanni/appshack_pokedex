package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpeciesInfo(
    @SerializedName("evolution_chain") val evoChain: EvoChain,
    @SerializedName("flavor_text_entries") val flavorTextEntries: List<FlavorText>) : Parcelable

@Parcelize
data class EvoChain(val url: String) : Parcelable

/*
@Parcelize
data class EvolutionChain()


@Parcelize
data class EvolvesTo()

@Parcelize
data class EvolutionDetails(
    val gender: String?,
    @SerializedName("held_item") val heldItem: String?,
    val item: PokemonItem?,
    @SerializedName("known_move") val knownMove: String?,
    @SerializedName("known_move_type") val knownMoveType: String?,
    
)



"gender": null,
"held_item": null,
"item": null,
"known_move": null,
"known_move_type": null,
"location": null,
"min_affection": null,
"min_beauty": null,
"min_happiness": null,
"min_level": 16,
"needs_overworld_rain": false,
"party_species": null,
"party_type": null,
"relative_physical_stats": null,
"time_of_day": "",
"trade_species": null,
"trigger": {
    "name": "level-up",
    "url": "https://pokeapi.co/api/v2/evolution-trigger/1/"
},
"turn_upside_down": false
*/