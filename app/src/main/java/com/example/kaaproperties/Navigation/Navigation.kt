package com.example.kaaproperties.Navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kaaproperties.Authentication.email.RegisterUser
//import com.example.kaaproperties.Authentication.email.WelcomeScreen
import com.example.kaaproperties.Authentication.email.loginUser
import com.example.kaaproperties.Authentication.email.welcomeScreen
import com.example.kaaproperties.PropertyViewModel
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.screens.location.AddingLocation
import com.example.kaaproperties.screens.location.locationsPage
import com.example.kaaproperties.screens.property.AddingProperty
import com.example.kaaproperties.screens.tenants.AddingTenants
import com.example.kaaproperties.screens.property.scaffoldForAllProperties
import com.example.kaaproperties.screens.tenants.scaffoldForAllTenants
import com.example.kaaproperties.screens.property.scaffoldForProperties
import com.example.kaaproperties.screens.tenants.Payments
import com.example.kaaproperties.screens.tenants.scaffoldForTenants
import com.example.kaaproperties.screens.user.UserProfileScreen

@Composable
fun Navigation(
    onEvents: (Events) -> Unit,
    states: states,
    context: Context,
    viewModel: PropertyViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Locations.route) {
        composable(route = Screens.SignUp.route) {
            RegisterUser(navController = navController,  context = context, viewModel = viewModel)
        }
        composable(route = Screens.LoginScreen.route) {
            loginUser(navController = navController, context = context, viewModel = viewModel)

        }
        composable(route = Screens.WelcomeScreen.route) {
            welcomeScreen(navController = navController)
        }
        composable(route = Screens.UserDetails.route) {
            UserProfileScreen(navController = navController, context = context, onEvents = onEvents, states = states)
        }
        composable(route = Screens.Locations.route) {
            locationsPage(states = states, onEvents = onEvents, navController = navController)
        }
        composable(route = Screens.AddingLocations.route) {
            AddingLocation(states = states, onEvent = onEvents, navController = navController, context = context)
        }
        composable(
            route = Screens.Property.route + "/{locationId}/{locationName}",
            arguments = listOf(
                navArgument("locationId"){
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("locationName"){
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            val locationId = it.arguments?.getString("locationId") ?: ""
            val locationName = it.arguments?.getString("locationName") ?: ""
            scaffoldForProperties(states = states, onEvents = onEvents, navController = navController, locationId = locationId, locationName = locationName)
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
                ?.let { it1 -> AddingProperty(states, onEvents, navController = navController,locationId = it1, context = context) }
        }
        composable(
            route = Screens.Tenants.route + "/{propertyId}/{propertyName}/{cost}",
            arguments = listOf(
                navArgument("propertyId"){
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("propertyName"){
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("cost"){
                    type = NavType.StringType
                    nullable = true
                }
            )
        ){
            val propertyId = it.arguments?.getString("propertyId") ?: ""
            val propertyName = it.arguments?.getString("propertyName") ?: ""
            val cost = it.arguments?.getString("cost") ?: ""
            scaffoldForTenants(states = states, onEvents = onEvents, navController = navController, propertyId = propertyId, context = context, propertyName = propertyName, cost = cost)
        }
        composable(
            route = Screens.AddingTenants.route + "/{propertyId}",
            arguments = listOf(
                navArgument("propertyId"){
                    type = NavType.StringType
                    nullable = true
                }
            )
        ){
            it.arguments?.getString("propertyId")
                ?.let { it1 -> AddingTenants(state = states, onEvent = onEvents, propertyId = it1, navController = navController, context = context) }
        }
        composable(
            route = Screens.PaymentByTenants.route + "/{propertyId}/{cost}/{propertyName}",
            arguments = listOf(
                navArgument("propertyId"){
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("cost"){
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("propertyName"){
                    type = NavType.StringType
                    nullable = true
                }
            )){
            val propertyId = it.arguments?.getString("propertyId") ?: ""
            val cost = it.arguments?.getString("cost") ?: ""
            val propertyName = it.arguments?.getString("propertyName") ?: ""
            Payments(onEvents = onEvents, context = context, navController = navController, states = states, propertyId = propertyId, propertyName = propertyName, cost = cost)
        }
        composable(route = Screens.AllProperty.route){
            scaffoldForAllProperties(states = states, onEvents = onEvents, navController = navController, context = context)
        }


    }

}
