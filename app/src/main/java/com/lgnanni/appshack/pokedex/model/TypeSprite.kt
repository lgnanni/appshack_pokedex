package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TypeSprite(val generationVIII: GenerationVIII) : Parcelable

@Parcelize
data class GenerationVIII(val swordShield: SwordShield) : Parcelable

@Parcelize
data class SwordShield(val nameIcon: String): Parcelable