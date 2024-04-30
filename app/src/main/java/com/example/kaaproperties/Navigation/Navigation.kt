package com.example.kaaproperties.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kaaproperties.Authentication.LoginUser
import com.example.kaaproperties.Authentication.RegisterUser
import com.example.kaaproperties.Events
import com.example.kaaproperties.room.entities.states
import com.example.kaaproperties.screens.AddingLocation
import com.example.kaaproperties.screens.ListofLocations
import com.example.kaaproperties.screens.UserProfileScreen

@Composable
fun Navigation(
    onEvents: (Events) -> Unit,
    states: states
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Locations.route) {
        composable(route = Screens.SignUp.route) {
            RegisterUser(navController = navController)
        }
        composable(route = Screens.LoginScreen.route) {
            LoginUser(navController = navController)
        }
        composable(route = Screens.UserDetails.route) {
            UserProfileScreen(navController = navController)
        }
        composable(route = Screens.Locations.route) {
            ListofLocations(states = states, onEvents = onEvents, navController = navController)
        }
        composable(route = Screens.AddingLocations.route) {
            AddingLocation(states = states, onEvent = onEvents, navController = navController)
        }
        composable(route = Screens.Property.route) {
            AddingLocation(states = states, onEvent = onEvents, navController = navController)
        }
    }

}