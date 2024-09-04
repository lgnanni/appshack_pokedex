package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TypeSpriteInfo(val sprites: TypeSprite): Parcelable

@Parcelize
data class TypeSprite(@SerializedName("generation-viii") val generationVIII: GenerationVIII) : Parcelable


@Parcelize
data class GenerationVIII(
    @SerializedName("sword-shield") val swordShield: SwordShield) : Parcelable

@Parcelize
data class SwordShield(@SerializedName("name_icon") val nameIcon: String): Parcelable