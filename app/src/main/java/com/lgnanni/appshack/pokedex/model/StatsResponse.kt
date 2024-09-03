package com.lgnanni.appshack.pokedex.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class StatsResponse(
    val baseStat: Int,
    val effort: Int,
    val stat: Stat,
) : Parcelable
