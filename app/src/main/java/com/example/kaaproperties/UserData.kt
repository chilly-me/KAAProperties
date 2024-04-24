package com.example.kaaproperties

import android.net.Uri

data class UserData(
    val userID: String = "",
    val email: String = "",
    val username: String = "",
    val profilePic: Uri?,
    val address: String = "",
    val age: Int = 0
)
