package com.example.kaaproperties.screens.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.logic.states

@Composable
fun descriptionPage(
    navController: NavController,
    onEvents: (Events) -> Unit,
    states: states,
    titleText: String,
    titleIcon: Int,
) {
     customScaffold(
         navController = navController,
         onEvents = onEvents,
         states = states,
         ifAdding = { /*TODO*/ },
         titleText = titleText,
         titleIcon = titleIcon,
         floatingActionButtonId = null,
     ) {
//        Image(painter = , contentDescription = )
     }
}