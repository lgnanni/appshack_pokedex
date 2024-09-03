package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Stat(
    val name: String,
) : Parcelable
