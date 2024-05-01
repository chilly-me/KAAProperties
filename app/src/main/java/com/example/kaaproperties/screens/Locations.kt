package com.example.kaaproperties.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.kaaproperties.logic.states

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
                                onEvents(Events.selectProperty(location.locationId))
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
                            Image(
                                painter = painterResource(id = R.drawable.profile),
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
                        Box(modifier = modifier
                            .weight(1f)
                            .padding(top = 10.dp), contentAlignment = Alignment.CenterEnd){
                            IconButton(onClick = { expanded.value = !expanded.value }) {
                                if (expanded.value){
                                    Icon(painter = painterResource(id = R.drawable.ic_show_less), contentDescription = "Show Less")
                                    cardHeight = 130.dp
                                }else{
                                    Icon(painter = painterResource(id = R.drawable.ic_show_more), contentDescription = "Show More")
                                    cardHeight = 100.dp
                                }
                            }
                        }



                    }

                }

            }
        }
    }
    Box(contentAlignment = Alignment.BottomEnd) {
        Button(onClick = {navController.navigate(route = "adding_locations_screen")}) {
            Text(text = "Add Location")
        }
    }
}