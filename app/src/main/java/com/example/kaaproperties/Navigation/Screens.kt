package com.example.kaaproperties.Navigation

sealed class Screens(val route: String) {

    data object WelcomeScreen: Screens("welcome_screen")
    data object LoginScreen: Screens("login_screen")
    data object SignUp: Screens("sign_up_screen")
    data object UserDetails: Screens("user_details_screen")

    data object Locations: Screens("location_screen")
    data object Property: Screens("property_screen")
    data object Tenants: Screens("tenants_screen")

    data object AddingProperty: Screens("adding_property_screen")
    data object AllProperty: Screens("all_property_screen")
    data object PaymentByTenants: Screens("payment_by_tenants_screen")

    data object TenantsStatus: Screens("tenants_status")
    fun withArgs(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach { arg->
                append("/$arg")
            }
        }
    }

}