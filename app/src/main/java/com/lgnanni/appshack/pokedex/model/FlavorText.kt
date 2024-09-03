package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FlavorText(val flavorText: String, val language: Language): Parcelable

@Parcelize
data class Language(val name: String) : Parcelable