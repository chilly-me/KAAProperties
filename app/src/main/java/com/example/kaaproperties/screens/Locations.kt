package com.example.kaaproperties.screens

import android.annotation.SuppressLint
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.ImagesList
import com.example.kaaproperties.logic.states


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun scaffoldForLocations(navController: NavController, onEvents: (Events) -> Unit, states: states) {
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
            IconButton(onClick = { /*TODO*/ }) {
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
                navController.navigate(Screens.AddingLocations.route)
            }
            ListofLocations(onEvents = onEvents, states = states, navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListofLocations(
    modifier: Modifier = Modifier,
    onEvents: (Events) -> Unit,
    states: states,
    navController: NavController
) {
    
    LazyColumn {
        items(states.locations){location ->
            Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(40.dp)) {
                var cardHeight = 100.dp
                Card(
                    onClick = {
                                onEvents(Events.selectLocation(location.locationId))
                        val _locationId = location.locationId.toString()
                        navController.navigate(Screens.Property.withArgs(_locationId))                              },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(cardHeight),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    var expanded = remember {
                        mutableStateOf(false)
                    }
                    Row(modifier = modifier
                        .fillMaxSize()
                        .padding(5.dp)) {
                        Box (modifier = modifier.weight(2f)){
                            val images = ImagesList().imagesforlocation.random().imageId
                            Image(
                                painter = painterResource(images),
                                contentDescription = "Real Estate 1",
                                Modifier
                                    .aspectRatio(2f)
//                            .size(40.dp)
                            )

                        }
                        Box(modifier = modifier.weight(2f)) {
                            Column {
                                Text(
                                    text = location.locationName,
                                    modifier = modifier
                                        .padding(12.dp),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold

                                )
                                Text(
                                    text = location.description,
                                    modifier = modifier
                                        .padding(12.dp),
                                    fontSize = 10.sp,

                                    )

                            }


                        }
                        Box(
                            modifier = modifier.weight(1f),
                            contentAlignment = Alignment.CenterEnd){
                            Column {
                                IconButton(onClick = { expanded.value = !expanded.value }) {
                                    if (expanded.value){
                                        Icon(painter = painterResource(id = R.drawable.ic_show_less), contentDescription = "Show Less")
                                        cardHeight = 130.dp
                                    }else{
                                        Icon(painter = painterResource(id = R.drawable.ic_show_more), contentDescription = "Show More")
                                        cardHeight = 100.dp
                                    }
                                }
//                                IconButton(onClick = { onEvents(Events.) })
//                                                            {
//
//                                }
                            }

                        }



                    }

                }

            }
        }
    }

}