package com.example.kaaproperties.Navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kaaproperties.Authentication.LoginUser
import com.example.kaaproperties.Authentication.RegisterUser
import com.example.kaaproperties.Authentication.loginUser
import com.example.kaaproperties.Events
import com.example.kaaproperties.room.entities.states
import com.example.kaaproperties.screens.AddingLocation
import com.example.kaaproperties.screens.AddingProperty
import com.example.kaaproperties.screens.ListofLocations
import com.example.kaaproperties.screens.UserProfileScreen
import com.example.kaaproperties.screens.propertyScreen

@Composable
fun Navigation(
    onEvents: (Events) -> Unit,
    states: states,
    context: Context
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.LoginScreen.route) {
        composable(route = Screens.SignUp.route) {
            RegisterUser(navController = navController)
        }
        composable(route = Screens.LoginScreen.route) {
            loginUser(navController = navController, context = context)
        }
        composable(route = Screens.UserDetails.route) {
            UserProfileScreen(navController = navController)
        }
        composable(route = Screens.Locations.route) {
            ListofLocations(states = states, onEvents = onEvents, navController = navController)
        }
        composable(route = Screens.AddingLocations.route) {
            AddingLocation(states = states, onEvent = onEvents, navController = navController, context = context)
        }
        composable(
            route = Screens.Property.route + "/{locationId}",
            arguments = listOf(
                navArgument("locationId"){
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            it.arguments?.getString("locationId")
                ?.let { it1 -> propertyScreen(states = states, onEvents, navController, locationId = it1) }
        }
        composable(
            route = Screens.AddingProperty.route + "/{locationId}",
            arguments = listOf(
                navArgument("locationId"){
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            it.arguments?.getString("locationId")
                ?.let { it1 -> AddingProperty(states, onEvents, navController,locationId = it1, context) }
        }
    }

}