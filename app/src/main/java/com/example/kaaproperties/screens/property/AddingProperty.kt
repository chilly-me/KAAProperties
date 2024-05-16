package com.example.kaaproperties.screens.property

import android.content.Context
import android.net.Uri
import android.util.Log
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



@Composable
fun AddingProperty(
    state: states,
    onEvent: (Events) -> Unit,
    locationId: String,
    navController: NavController,
    context: Context,
) {
    val _locationId = locationId.toInt()
    onEvent(Events.setlocationId(_locationId))

    var ImageUriList by remember {
        mutableStateOf<List<Uri?>>(emptyList())
    }
    customDailog(
        onDismiss = {
            onEvent(Events.NotAdding)
            navController.navigate(Screens.Property.withArgs(locationId)) {
                navController.popBackStack()
                Log.d("isStackPopped", "${navController.popBackStack()}")
            }
        },
        headerText = "Add Property",
        content = {
            customTextField2(
                value = state.propertyName,
                onValueChange = {
                    onEvent(
                        Events.setPropertyName(it)
                    )
                },
                placeHolder = "Alpha Courts",
                iconId = null
            )
            customTextField2(
                value = state.propertyAddress,
                onValueChange = {
                    onEvent(
                        Events.setpropertyAddress(it)
                    )
                },
                placeHolder = "23-30100 Eldoret",
                iconId = null
            )

            customTextField2(
                value = state.propertyDescription,
                onValueChange = {
                    onEvent(
                        Events.setpropertyDescription(it)
                    )
                },
                placeHolder = "One bedroom",
                iconId = null
            )
            customTextField2(
                value = state.capacity,
                onValueChange = {
                    onEvent(
                        Events.setcapacity(it)
                    )
                },
                placeHolder = "40",
                iconId = null
            )
            customTextField2(
                value = state.propertyCost,
                onValueChange = {
                    onEvent(
                        Events.setCost(it)
                    )
                },
                placeHolder = "Ksh. 10,000",
                iconId = null
            )
        },
        imageUriList = {ImageUriList = it},
        saveButton = {
            customButton(
                onClick = {
                    onEvent(Events.saveProperty(ImageUriList))
                    navController.navigate(Screens.Property.withArgs(locationId)) {
                        navController.popBackStack()
                        Log.d("isStackPopped", "${navController.popBackStack()}")
                    }
                },
                buttonText = "Save Property",
                iconId = R.drawable.baseline_save_24,
                color = 0xDA309FFF
            )
        }
    )

}
