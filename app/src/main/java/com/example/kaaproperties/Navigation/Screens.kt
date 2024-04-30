package com.example.kaaproperties.Navigation

sealed class Screens(val route: String) {
    object LoginScreen: Screens("login_screen")
    object SignUp: Screens("sign_up_screen")
    object UserDetails: Screens("user_details_screen")
}