package com.example.firebase.onBoard

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataOnboard (
    val image:Int,
    val textOne:String,
    val textTwo: String
):Parcelable