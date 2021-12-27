package com.android.bishkekroutes.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Route(
    val x: List<Double> = emptyList(),
    val y: List<Double> = emptyList(),
    val streets: List<String> = emptyList(),
    val length: Double = 0.0,
    val link: List<String> = emptyList(),
    val description: String = ""
): Parcelable