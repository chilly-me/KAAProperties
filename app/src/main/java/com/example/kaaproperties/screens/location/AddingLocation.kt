package com.example.kaaproperties.screens.location

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.screens.components.customButton
import com.example.kaaproperties.screens.components.customDailog
import com.example.kaaproperties.screens.components.customTextField2
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddingLocation(
    onEvent: (Events) -> Unit,
    states: states,
    navController: NavController,
    context: Context,
) {
    var imageUriList by remember {
        mutableStateOf<List<Uri?>>(emptyList())
    }

    customDailog(
        onDismiss = {
            if (states.active){
                onEvent(Events.NotAdding)
                navController.navigate(Screens.Locations.route) {
                    navController.popBackStack()
                    Log.d("isStackPopped", "${navController.popBackStack()}")
                }
            }

        },
        headerText = "Add Location",

        content = {
            customTextField2(
                value = states.locationName,
                onValueChange = { onEvent(
                    Events.setLocationName(it)
                ) },
                placeHolder = "Location Name",
                isEnabled = states.active,
                iconId = R.drawable.baseline_location_on_24
            )
            customTextField2(
                value = states.locationDescription,
                onValueChange = {
                    onEvent(
                        Events.setLocationDescription(it)
                    )
                },
                placeHolder = "Location Description",
                isEnabled = states.active,
                iconId = null,
                singleLine = false
            )
        },
        imageUriList = {
            imageUriList = it
        },
        saveButton = {
            customButton(
                onClick = {
                    onEvent(Events.saveLocation(imageUriList))
                    navController.navigate(Screens.Locations.route) {
                        navController.popBackStack()
                        android.util.Log.d("isStackPopped", "${navController.popBackStack()}")
                    }
                },
                buttonText = "Save Location",
                iconId = R.drawable.baseline_save_24,
                color = 0xDA309FFF
            )
        },

    )

}
fun signOutUser(googleSignInClient: GoogleSignInClient) {
    googleSignInClient.signOut()
}