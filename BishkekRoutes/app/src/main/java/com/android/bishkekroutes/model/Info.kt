package com.android.bishkekroutes.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Info (
    val x: List<Double> = emptyList(),
    val y: List<Double> = emptyList(),
    val link: List<String> = emptyList(),
    val description: String = ""
): Parcelable