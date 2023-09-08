package com.team2.contactapp

object Util {
    fun String.phoneNumFormat() : String {
        val str = this
        return when (str.length) {
            9 -> buildString {
                append(str.substring(0,2)).append("-").append(str.substring(2,5)).append("-").append(str.substring(5))
            }

            11 -> buildString {
                append(str.substring(0,3)).append("-").append(str.substring(3,7)).append("-").append(str.substring(7))
            }

            else -> this
        }
    }
}