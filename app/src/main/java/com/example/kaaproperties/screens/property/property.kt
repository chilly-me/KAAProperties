package com.example.kaaproperties.screens.property

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.ImagesList
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.screens.components.customCard
import com.example.kaaproperties.screens.components.customScaffold


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun scaffoldForProperties(
    navController: NavController,
    onEvents: (Events) -> Unit,
    states: states,
    locationId: String,
) {
    customScaffold(
        navController = navController,
        onEvents = onEvents,
        states = states,
        ifAdding = {
            Log.d("AddingProperty", "1")
            navController.navigate(Screens.AddingProperty.withArgs(locationId))

        },
        titleText = "Properties for ${states.locationName}",
        titleIcon = R.drawable.baseline_location_city_24,
        screen = {
            Column(modifier = Modifier) {
                var isFiltering by remember {
                    mutableStateOf(false)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = { isFiltering = !isFiltering }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = null
                        )
                    }
                }
                if (isFiltering) {
                    filterProperties(onEvents, states, navController)
                }
                propertyScreen(
                    onEvents = onEvents,
                    states = states,
                    navController = navController,
                    locationId = locationId
                )
            }
        }


    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun filterProperties(onEvents: (Events) -> Unit, states: states, navController: NavController) {
    AlertDialog(
        onDismissRequest = { navController.navigate(Screens.Property.withArgs(states.locationId.toString())) },
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
    locationId: String,
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
                                property.propertyName
                            )
                        )
                    },
                    navigation = { navController.navigate(Screens.Tenants.withArgs(_propertyId)) },
                    onDeleteEvent = { onEvents(Events.deleteProperty(property)) },
                    text1 = property.propertyName,
                    text2 = property.propertyDescription,
                    text3 = property.propertyAddress,
                    text4 = property.capacity,
                    id = property.propertyId.toString(),
                    collectionPath = "Property",
                    errorId = R.drawable.property2
                )


            }
        }
    }

}
