package com.example.kaaproperties.screens.location

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.screens.components.customCard
import com.example.kaaproperties.screens.components.customScaffold
import com.example.kaaproperties.screens.components.customSearchBox


@Composable
fun locationsPage(
    navController: NavController,
    onEvents: (Events) -> Unit,
    states: states,

    ) {
    customScaffold(
        navController = navController,
        onEvents = onEvents,
        states = states,
        ifAdding = { navController.navigate(Screens.AddingLocations.route) },
        titleText = "Locations",
        locationSelected = true,
        titleIcon = R.drawable.baseline_location_on_24,
        screen = {
            Column {
                customSearchBox(onValueChange = { onEvents(Events.searchLocation(it)) })
                ListofLocations(onEvents = onEvents, states = states, navController = navController)
            }

        }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListofLocations(
    modifier: Modifier = Modifier,
    onEvents: (Events) -> Unit,
    states: states,
    navController: NavController,
) {

    LazyColumn {
        items(states.locations) { location ->
            Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(40.dp)) {
                val _locationId = location.locationId.toString()
                var cardHeight = 100.dp
                customCard(
                    navigation = { navController.navigate(Screens.Property.withArgs(_locationId)) },
                    onEvent = { onEvents(Events.selectLocation(location.locationId, location.locationName)) },
                    onDeleteEvent = { onEvents(Events.deleteLocation(location)) },
                    text1 = location.locationName,
                    text2 = location.description,
                    imageList = location.locationImages,
                    errorId = R.drawable.location1
                )

            }
        }
    }

}

@Composable
fun propertyPage(
    navController: NavController,
    onEvents: (Events) -> Unit,
    states: states,
) {
    customScaffold(
        navController = navController,
        onEvents = onEvents,
        states = states,
        ifAdding = { navController.navigate(Screens.AddingProperty.withArgs()) },
        titleIcon = R.drawable.baseline_location_city_24,
        titleText = "Properties in ${states.locationName}"
    )
    {

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun locationPreview(modifier: Modifier = Modifier) {
    val brush = Brush.verticalGradient(
        listOf(
            Color(0xDA035092),
            Color(0xFF333481),
            Color(0xFF73738A),
            Color(0xFFF2F1F6),
            )

    )
    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier,
                containerColor = Color(0xFFF2F1F6),
                contentColor = Color(0xDA035092),
            ) {
                NavigationBarItem(
                    selected = true, onClick = { /*TODO*/ },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_email),
                            contentDescription = "Email",
                        )
                    },
                )
                NavigationBarItem(
                    selected = true, onClick = { /*TODO*/ },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_email),
                            contentDescription = "Email",
                        )
                    },
                )
                NavigationBarItem(
                    selected = true, onClick = { /*TODO*/ },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_email),
                            contentDescription = "Email",
                        )
                    },
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout(
                modifier = modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .background(Color(0xDA035092))
            ) {
                val (topImg, profile, title, icon, user) = createRefs()

                Image(
                    painter = painterResource(id = R.drawable.baseline_location_on_24),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp)
                        .constrainAs(icon) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Image(
                    painter = painterResource(id = R.drawable.arc_3edited),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color(0xDA035092)),
                    modifier = Modifier
                        .constrainAs(topImg){
                            top.linkTo(parent.bottom)
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.profilelogo),
                    contentDescription = null,
                    modifier = Modifier
                        .constrainAs(profile){
                            end.linkTo(parent.end, margin = 15.dp)
                            top.linkTo(parent.top, margin = 15.dp)
                        },
//                        .clickable { navController.navigate(Screens.UserDetails.route) },
                    colorFilter = ColorFilter.tint(Color.White)
                )

                Text(
                    text = "Locations",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 30.sp,
                    ),
                    modifier = modifier
                        .constrainAs(title) {
                            top.linkTo(parent.top, margin = 15.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom, margin = 25.dp)
                        }


                )



            }
        }
    }

}