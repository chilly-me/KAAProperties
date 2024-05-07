package com.example.kaaproperties.screens.property

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.ImagesList
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.screens.components.customCard


@Composable
fun scaffoldForProperties(navController: NavController, onEvents: (Events) -> Unit, states: states, locationId: String) {
    Scaffold(
        topBar = { Row(modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(10.dp)) {
            Text(
                text = "KAA Properties",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { navController.navigate(Screens.UserDetails.route) }) {
                Icon(painter = painterResource(id = R.drawable.baseline_person_24), contentDescription = "Profile")
            }
        }},
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { navController.navigate(Screens.Locations.route) },
                    icon = { Icon(painter = painterResource(id = R.drawable.baseline_location_on_24), contentDescription = "Home") },
                    label = { Text(text = "Locations") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Screens.AllProperty.route) },
                    icon = { Icon(painter = painterResource(id = R.drawable.baseline_location_city_24), contentDescription = "Property") },
                    label = { Text(text = "All Properties") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Screens.AllTenants.route) },
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_groups), contentDescription = "Tenants") },
                    label = { Text(text = "All Tenants") }
                )

            }
        },
        floatingActionButton = {
            IconButton(onClick = { onEvents(Events.Adding)}) {
                Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "Add Location")
            }
        },

        ) {
        Column(modifier = Modifier.padding(it)) {
            if (states.isAdding){
                navController.navigate(Screens.AddingProperty.withArgs(locationId))
            }
            propertyScreen(onEvents = onEvents, states = states, navController = navController, locationId = locationId)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun propertyScreen(states: states, onEvents: (Events)-> Unit, navController: NavController, locationId: String) {

    LazyColumn {
        items(states.property){property ->
            val _propertyId = property.propertyId.toString()
            Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(40.dp)) {


                customCard(
                    onEvent = { onEvents(Events.selectProperty(property.propertyId)) },
                    navigation = { navController.navigate(Screens.Tenants.withArgs(_propertyId)) },
                    onDeleteEvent = { onEvents(Events.deleteProperty(property)) },
                    text1 = property.propertyName,
                    text2 = property.propertyDescription,
                    text3 = property.propertyAddress,
                    text4 = property.capacity,)


            }
        }
    }

}