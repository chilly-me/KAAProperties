package com.example.kaaproperties.Navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kaaproperties.Authentication.email.RegisterUser
//import com.example.kaaproperties.Authentication.email.WelcomeScreen
import com.example.kaaproperties.Authentication.email.loginUser
import com.example.kaaproperties.Authentication.email.welcomeScreen
import com.example.kaaproperties.PropertyViewModel
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.screens.location.locationsPage
import com.example.kaaproperties.screens.property.scaffoldForAllProperties
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
    NavHost(navController = navController, startDestination = Screens.WelcomeScreen.route) {
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
            locationsPage(states = states, onEvents = onEvents, navController = navController, context = context)
//            AllPayments()
        }
        composable(
            route = Screens.Property.route
        ) {
            scaffoldForProperties(states = states, onEvents = onEvents, navController = navController, context = context)
        }
        composable(
            route = Screens.Tenants.route,

        ){
            scaffoldForTenants(states = states, onEvents = onEvents, navController = navController, context = context,)
        }
        composable(
            route = Screens.PaymentByTenants.route)
        {

            Payments(onEvents = onEvents, context = context, navController = navController, states = states)
        }
        composable(route = Screens.AllProperty.route){
            scaffoldForAllProperties(states = states, onEvents = onEvents, navController = navController, context = context)
        }
        composable(route = "additional_information"){
            scaffoldForAllProperties(states = states, onEvents = onEvents, navController = navController, context = context)
        }


    }

}
