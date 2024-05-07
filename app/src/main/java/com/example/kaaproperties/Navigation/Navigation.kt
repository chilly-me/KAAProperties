package com.example.kaaproperties.Navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kaaproperties.Authentication.email.RegisterUser
import com.example.kaaproperties.Authentication.email.loginUser
import com.example.kaaproperties.AuthenticationViewModel
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.screens.location.AddingLocation
import com.example.kaaproperties.screens.property.AddingProperty
import com.example.kaaproperties.screens.tenants.AddingTenants
import com.example.kaaproperties.screens.tenants.PayingRent
import com.example.kaaproperties.screens.user.ScaffoldforUserProfile
import com.example.kaaproperties.screens.property.scaffoldForAllProperties
import com.example.kaaproperties.screens.tenants.scaffoldForAllTenants
import com.example.kaaproperties.screens.location.scaffoldForLocations
import com.example.kaaproperties.screens.property.scaffoldForProperties
import com.example.kaaproperties.screens.tenants.scaffoldForTenants

@Composable
fun Navigation(
    onEvents: (Events) -> Unit,
    states: states,
    context: Context,
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Locations.route) {
        composable(route = Screens.UserDetails.route) {
            ScaffoldforUserProfile(navController = navController)
        }
        composable(route = Screens.Locations.route) {
            scaffoldForLocations(states = states, onEvents = onEvents, navController = navController)
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
                ?.let { it1 -> scaffoldForProperties(states = states, onEvents = onEvents, navController = navController, locationId = it1) }
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
        composable(
            route = Screens.Tenants.route + "/{propertyId}",
            arguments = listOf(
                navArgument("propertyId"){
                    type = NavType.StringType
                    nullable = true
                }
            )
        ){
            it.arguments?.getString("propertyId")?.let { it1 -> scaffoldForTenants(states = states, onEvents = onEvents, navController = navController, propertyId = it1, context = context) }
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
        composable(route = Screens.TenantsStatus.route){
            PayingRent(onEvents = onEvents, context = context, navController = navController, states = states)
        }
        composable(route = Screens.AllProperty.route){
            scaffoldForAllProperties(states = states, onEvents = onEvents, navController = navController, context = context)
        }
        composable(route = Screens.AllTenants.route){
            scaffoldForAllTenants(states = states, onEvents = onEvents, navController = navController, context = context)
        }

    }

}

@Composable
fun NavigationForAuthentication(context: Context, viewModel: AuthenticationViewModel)  {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.LoginScreen.route){
        composable(route = Screens.SignUp.route) {
            RegisterUser(navController = navController,  context = context, viewModel = viewModel)
        }
        composable(route = Screens.LoginScreen.route) {
            loginUser(navController = navController, context = context, viewModel = viewModel)
        }
    }

}