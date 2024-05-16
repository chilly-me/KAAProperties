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
                    navigation = { navController.navigate(Screens.Property.withArgs(_locationId, location.locationName)) },
                    onEvent = { onEvents(Events.selectLocation(location.locationId)) },
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
