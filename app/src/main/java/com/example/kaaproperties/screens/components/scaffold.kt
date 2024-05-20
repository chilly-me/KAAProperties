@file:Suppress("UNUSED_EXPRESSION")

package com.example.kaaproperties.screens.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.logic.states

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun customScaffold(
    modifier: Modifier = Modifier,
    navController: NavController,
    onEvents: (Events) -> Unit,
    states: states,
    ifAdding: @Composable () -> Unit,
    titleText: String,
    locationSelected: Boolean = false,
    propertiesSelected: Boolean = false,
    profileSelected: Boolean = false,
    floatingActionButtonId: Int? = R.drawable.ic_add,
    titleIcon: Int,
    screen: @Composable () -> Unit
) {
    val brush = Brush.verticalGradient(
        listOf(
            Color(0xFFF2F1F6),
            Color(0xFFF2F1F6),
            Color(0xDA035092),
            )

    )
    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier,
                containerColor = Color(0xDA035092),
                contentColor = Color(0xFFF2F1F6),
            ) {
                val tint =
                NavigationBarItem(
                    selected = locationSelected,
                    onClick = { navController.navigate(Screens.Locations.route) },
                    icon = { Icon(
                        painter = painterResource(id = R.drawable.baseline_location_on_24),
                        contentDescription = "Home",
                        tint =  if (locationSelected) {
                            Color.Black
                        } else {
                            Color.White
                        }
                    ) },
                    label = { Text(text = "Locations", color = Color.White) }
                )
                NavigationBarItem(
                    selected = propertiesSelected,
                    onClick = { navController.navigate(Screens.AllProperty.route) },
                    icon = { Icon(
                        painter = painterResource(id = R.drawable.baseline_location_city_24),
                        contentDescription = "Payments",
                        tint = if (propertiesSelected) {
                            Color.Black
                        } else {
                            Color.White
                        }
                    ) },
                    label = { Text(text = "All Properties ", color = Color.White) }
                )
                NavigationBarItem(
                    selected = profileSelected,
                    onClick = { navController.navigate(Screens.UserDetails.route) },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_person_24),
                            contentDescription = "Profile",
                            tint = if (profileSelected) {
                                Color.Black
                            } else {
                                Color.White
                            }
                        ) },
                    label = { Text(text = "Profile", color = Color.White) }
                )

            }
        },
        floatingActionButton = {
            IconButton(onClick = { onEvents(Events.Adding)}) {
                floatingActionButtonId?.let { painterResource(id = it) }
                    ?.let { Icon(painter = it, contentDescription = null) }
            }
        },
        )
     {
         Column(
             modifier = Modifier
                 .padding(it)
                 .fillMaxSize()
                 .background(brush),
             horizontalAlignment = Alignment.CenterHorizontally
         ) {
             if (states.isAdding){
                 ifAdding()
             }
             ConstraintLayout(
                 modifier = modifier
                     .height(125.dp)
                     .background(Color(0xDA035092))
             ) {
                 val (topImg, title, icon, user) = createRefs()


                 Image(
                     painter = painterResource(id = R.drawable.arc_3),
                     contentDescription = null,
                     modifier = Modifier
                         .fillMaxWidth()
                         .constrainAs(topImg) {
                             bottom.linkTo(parent.bottom)
                         },
                     contentScale = ContentScale.FillBounds

                 )
                 Image(
                     painter = painterResource(id = titleIcon),
                     contentDescription = null,
                     modifier = Modifier
                         .clip(CircleShape)
                         .size(40.dp)
                         .constrainAs(icon) {
                             start.linkTo(parent.start)
                             end.linkTo(parent.end)
                             bottom.linkTo(topImg.bottom)
                         },
                     contentScale = ContentScale.Crop,
                     colorFilter = ColorFilter.tint(Color.Gray)
                 )

                 Text(
                     text = titleText,
                     style = TextStyle(
                         color = Color.White,
                         fontSize = 25.sp,
                     ),
                     modifier = modifier
                         .constrainAs(title) {
                             top.linkTo(parent.top, margin = 25.dp)
                             start.linkTo(parent.start, margin = 25.dp)
                         }


                 )
             }

             Spacer(modifier = Modifier.height(20.dp))

             screen()
         }
    }
}
