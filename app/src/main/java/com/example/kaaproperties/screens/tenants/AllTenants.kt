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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.screens.components.customScaffold
import com.example.kaaproperties.screens.components.customSearchBox
import com.example.kaaproperties.ui.theme.AlegreyoSansFontFamily

@Composable
fun scaffoldForAllTenants(
    navController: NavController,
    onEvents: (Events) -> Unit,
    states: states,
    context: Context,
) {
    customScaffold(
        navController = navController,
        onEvents = onEvents,
        states = states,
        ifAdding = {  },
        titleText = "All Tenants",
        titleIcon = R.drawable.ic_groups,
        floatingActionButtonId = null,
        tenantsSelected = true,
        screen = {
            Column(modifier = Modifier) {

                customSearchBox(onValueChange = { onEvents(Events.searchTenant(it)) })
                AllTenants(navController, states, onEvents)
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTenants(navController: NavController, state: states, onEvents: (Events) -> Unit) {
    if (state.tenants.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "There are no tenants with that name ", color = Color.Black, fontFamily = AlegreyoSansFontFamily, fontWeight = FontWeight(1000))
        }
    }
    LazyColumn {
        items(state.tenants) { tenant ->
            Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(40.dp)) {
                Card(
                    onClick = {/**/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(100.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    var expanded = remember {
                        mutableStateOf(false)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                    ) {
                        Box(modifier = Modifier.weight(2f)) {
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
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 10.dp), contentAlignment = Alignment.CenterEnd
                        ) {
                            IconButton(onClick = { expanded.value = !expanded.value }) {
                                if (expanded.value) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_show_less),
                                        contentDescription = "Show Less"
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_show_more),
                                        contentDescription = "Show More"
                                    )
                                }
                            }
                        }


                    }

                }

            }
        }
    }

}

