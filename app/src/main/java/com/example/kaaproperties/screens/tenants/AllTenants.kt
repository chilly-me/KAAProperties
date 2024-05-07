package com.example.kaaproperties.screens.tenants

import android.content.Context
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
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.logic.states

@Composable
fun scaffoldForAllTenants(navController: NavController, onEvents: (Events) -> Unit, states: states, context: Context) {
    onEvents(Events.showTenants)
    Scaffold(
        topBar = { Row(modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(10.dp)) {
            Text(
                text = "All Tenants ",
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
                    selected = false,
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
                    selected = true,
                    onClick = { navController.navigate(Screens.AllTenants.route) },
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_groups), contentDescription = "Tenants") },
                    label = { Text(text = "All Tenants") }
                )

            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            AllTenants(navController, states, onEvents)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTenants(navController: NavController, state: states, onEvents: (Events) -> Unit) {
    onEvents(Events.showTenants)
    LazyColumn {
        items(state.tenants){tenant ->
            Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(40.dp)) {
                Card(
                    onClick = {/**/},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(100.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    var expanded = remember{
                        mutableStateOf(false)
                    }
                    Row(modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)) {
                        Box (modifier = Modifier.weight(2f)){
                            Image(
                                painter = painterResource(id = R.drawable.profile),
                                contentDescription = "Real Estate 1",
                                Modifier
                                    .aspectRatio(2f)
//                            .size(40.dp)
                            )

                        }
                        Box(modifier = Modifier.weight(2f)) {
                            Column {
                                Text(
                                    text = tenant.fullName,
                                    modifier = Modifier
                                        .padding(12.dp),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold

                                )
                                Text(
                                    text = tenant.email,
                                    modifier = Modifier
                                        .padding(5.dp),
                                    fontSize = 8.sp,

                                    )
                                Text(
                                    text = tenant.phoneNumber,
                                    modifier = Modifier
                                        .padding(5.dp),
                                    fontSize = 8.sp,

                                    )

                            }


                        }
                        Box(modifier = Modifier
                            .weight(1f)
                            .padding(top = 10.dp), contentAlignment = Alignment.CenterEnd){
                            IconButton(onClick = { expanded.value = !expanded.value }) {
                                if (expanded.value){
                                    Icon(painter = painterResource(id = R.drawable.ic_show_less), contentDescription = "Show Less")
                                }else{
                                    Icon(painter = painterResource(id = R.drawable.ic_show_more), contentDescription = "Show More")
                                }
                            }
                        }



                    }

                }

            }
        }
    }

}