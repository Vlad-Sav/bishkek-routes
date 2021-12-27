package com.android.bishkekroutes.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Place ( val x: Double = 0.0,
                   val y: Double = 0.0,
                   val name: String = "",
                   val address: String  = "",
                   val link: List<String> = emptyList(),
                   val description: String = ""): Parcelable