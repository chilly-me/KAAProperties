package com.example.kaaproperties.screens.property

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.screens.components.CustomDropDown
import com.example.kaaproperties.screens.components.customCard
import com.example.kaaproperties.screens.components.customScaffold


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun scaffoldForProperties(
    navController: NavController,
    onEvents: (Events) -> Unit,
    states: states,
    context: Context
) {
    Log.d("locationId", states.selectedLocation.locationId.toString() + "in property")

    customScaffold(
        navController = navController,
        onEvents = onEvents,
        states = states,
        ifAdding = {
            Log.d("AddingProperty", "1")
            AddingProperty(states, onEvents, navController = navController, context = context)
        },

        titleText = "Properties in ${states.selectedLocation.locationName}",
        titleIcon = R.drawable.baseline_location_city_24,
        screen = {
            Column(modifier = Modifier) {
//                var isFiltering by remember {
//                    mutableStateOf(false)
//                }
//                val items = listOf<Pair<String, Int?>>(
//                    Pair("One Bedroom", null),
//                    Pair("Two Bedroom", null),
//                    Pair("Three Bedroom", null)
//                )
//                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
//                    CustomDropDown(items, onClick = {Log.d("dropDown", "$it clicked")})
//                }
//                if (isFiltering) {
//                    filterProperties(onEvents, states, navController)
//                }
                propertyScreen(
                    onEvents = onEvents,
                    states = states,
                    navController = navController,
                )
            }
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun filterProperties(onEvents: (Events) -> Unit, states: states, navController: NavController) {
    AlertDialog(
        onDismissRequest = { navController.navigate(Screens.Property.route) },
        confirmButton = { Text(text = "Ok") },
        dismissButton = { Text(text = "Dismiss") },
        title = { Text(text = "Filter Property") },
        text = {
            Column {
                Text(
                    text = "Using Description",
                    modifier = Modifier.clickable {
                        onEvents(
                            Events.filterPropertiesbyDescription(
                                "One Bedroom",
                                states.locationId
                            )
                        )
                    }
                )
                Text(
                    text = "Using Capacity",
                    modifier = Modifier.clickable {
                        onEvents(Events.filterPropertiesbyCapacity("25", states.locationId))
                    }
                )
            }
        },
//        modifier = Modifier.size(400.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun propertyScreen(
    states: states,
    onEvents: (Events) -> Unit,
    navController: NavController,
) {

    LazyColumn {
        items(states.property) { property ->
            val _propertyId = property.propertyId.toString()
            Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(40.dp)) {

                customCard(
                    onEvent = {
                        onEvents(
                            Events.selectProperty(
                                property.propertyId,
                            )
                        )
                    },
                    navigation = { navController.navigate(Screens.Tenants.route) },
                    onDeleteEvent = { onEvents(Events.deleteProperty(property)) },
                    text1 = property.propertyName,
                    text2 = property.propertyDescription,
                    text3 = property.propertyAddress,
                    text4 = property.capacity,
                    imageList = property.propertyImages,
                    errorId = R.drawable.property2,
                    price = property.cost
                )


            }
        }
    }

}
