package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sprites (val backDefault: String, val backShiny: String, val frontDefault: String, val frontShiny: String, val other: OtherArtwork): Parcelable


@Parcelize
data class OtherArtwork(val officialArtwork: OfficialSprites) : Parcelable

@Parcelize
data class OfficialSprites(val frontDefault: String, val frontShiny: String): Parcelable