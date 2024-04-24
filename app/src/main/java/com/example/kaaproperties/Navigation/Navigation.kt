package com.example.kaaproperties.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kaaproperties.Authentication.LoginUser
import com.example.kaaproperties.Authentication.RegisterUser

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.SignUp.route){
        composable(route = Screens.SignUp.route){
            RegisterUser(navController = navController)
        }
        composable(route = Screens.LoginScreen.route){
            LoginUser(navController = navController)
        }
    }

}