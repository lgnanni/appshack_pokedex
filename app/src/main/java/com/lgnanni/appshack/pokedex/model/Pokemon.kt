package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class Pokemon(
  val id: Int,
  val name: String,
  val sprites: Sprites,
  val species: PokemonUrl,
  val cries: Cries,
  val types: List<Types>
) : Parcelable

@Parcelize
data class PokemonUrl(val name: String, val url: String): Parcelable