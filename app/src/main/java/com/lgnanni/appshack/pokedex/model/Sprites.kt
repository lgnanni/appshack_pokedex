package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sprites (val other: OtherArtwork): Parcelable


@Parcelize
data class OtherArtwork(
    @SerializedName("official-artwork") val officialArtwork: OfficialSprites) : Parcelable

@Parcelize
data class OfficialSprites(
    @SerializedName("front_default") val frontDefault: String,
    @SerializedName("front_shiny") val frontShiny: String): Parcelable