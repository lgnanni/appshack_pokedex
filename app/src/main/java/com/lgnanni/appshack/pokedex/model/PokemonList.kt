package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonList(val count: Int, val next: String?, val previous: String?, val results: List<PokemonListItem>) : Parcelable


@Parcelize
data class PokemonListItem(val name: String, val url: String): Parcelable