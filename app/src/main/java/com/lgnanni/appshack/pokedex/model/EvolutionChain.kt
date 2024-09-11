package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class EvolutionChain(val url: String) : Parcelable

@Parcelize
data class Chain(val chain: EvoChain): Parcelable

@Parcelize
data class EvoChain(val species: PokemonUrl,
                 @SerializedName("evolution_details") val evoDetails: List<EvolutionDetails>,
                 @SerializedName("evolves_to") val evolvesTo: List<EvoChain>) : Parcelable

@Parcelize
data class EvolutionDetails(
    val gender: String?,
    @SerializedName("held_item") val heldItem: PokemonUrl?,
    val item: PokemonUrl?,
    @SerializedName("known_move") val knownMove: PokemonUrl?,
    @SerializedName("known_move_type") val knownMoveType: PokemonUrl?,
    val location: PokemonUrl?,
    @SerializedName("min_affection") val minAffection: Int?,
    @SerializedName("min_happiness") val minHappiness: Int?,
    @SerializedName("min_beauty") val minBeauty: Int?,
    @SerializedName("min_level") val minLevel: Int?,
    @SerializedName("needs_overworld_rain") val needsOverwoldRain: Boolean,
    @SerializedName("party_species") val partySpecies: PokemonUrl?,
    @SerializedName("party_type") val partyType: PokemonUrl?,
    @SerializedName("relative_physical_stats") val relPhysStats: PokemonUrl?,
    @SerializedName("time_of_day") val timeOfDay: String,
    @SerializedName("trade_species") val tradeSpecies: PokemonUrl?,
    val trigger: PokemonUrl,
    @SerializedName("turn_upside_down") val turnUpside: Boolean,
) : Parcelable {
    constructor(): this (
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            false,
            null,
            null,
            null,
            "",
            null,
            PokemonUrl("", ""),
            false
        )
}

@Parcelize
data class EvolutionTrigger(val names: List<EvoTriggerLanguage>): Parcelable {
    constructor(): this(emptyList())
}

@Parcelize
data class EvolutionItem(@SerializedName("effect_entries") val effectEntries:List<ItemEffect>): Parcelable

@Parcelize
data class ItemEffect(val language: PokemonUrl, @SerializedName("short_effect") val effect: String) : Parcelable

@Parcelize
data class EvoTriggerLanguage(val name: String, val language: PokemonUrl): Parcelable
