package com.example.kaaproperties

data class UserData(
    val userID: String = "",
    val email: String = "",
    val username: String = "",
    val profilePic: HashMap<String, Any>,
    val address: String = "",
    val age: String = ""
)
