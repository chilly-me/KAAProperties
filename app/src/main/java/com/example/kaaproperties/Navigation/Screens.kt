package com.example.kaaproperties.Navigation

sealed class Screens(val route: String) {
    data object LoginScreen: Screens("login_screen")
    data object SignUp: Screens("sign_up_screen")
    data object UserDetails: Screens("user_details_screen")

    data object Locations: Screens("location_screen")
    data object Property: Screens("property_screen")
    data object Tenants: Screens("tenants_screen")

    data object AddingLocations: Screens("adding_locations_screen")
    data object AddingProperty: Screens("adding_property_screen")
    data object AddingTenants: Screens("adding_tenants_screen")

    fun withArgs(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach { arg->
                append("/$arg")
            }
        }
    }

}