package com.example.kaaproperties.screens.property

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.screens.components.customCard
import com.example.kaaproperties.screens.components.customScaffold
import com.example.kaaproperties.screens.components.customSearchBox

@Composable
fun scaffoldForAllProperties(
    navController: NavController,
    onEvents: (Events) -> Unit,
    states: states,
    context: Context,
) {
    LaunchedEffect(key1 = states.key) {
        onEvents(Events.showProperies)
    }
    customScaffold(
        navController = navController,
        onEvents = onEvents,
        states = states,
        ifAdding = { },
        titleText = "All Properties",
        titleIcon = R.drawable.baseline_location_city_24,
        propertiesSelected = true,
        floatingActionButtonId = null

    ) {
        Column(modifier = Modifier) {
            customSearchBox(onValueChange = { onEvents(Events.searchProperties(it)) })
            AllProperty(states, onEvents, navController, context)
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllProperty(
    states: states,
    onEvents: (Events) -> Unit,
    navController: NavController,
    context: Context,
) {
    LazyColumn {
        items(states.property) { property ->
            Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(40.dp)) {
                val _propertyId = property.propertyId.toString()
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
                    imageList = property.propertyImages,
                    errorId = R.drawable.property2
                    )


            }
        }
    }
}