package com.team2.contactapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    //val id : Int,
    val name : String,
    val imgRes : Int,
    val phoneNumber: String,
    val email : String,
    val event : String,
    val memo : String,
) : Parcelable
