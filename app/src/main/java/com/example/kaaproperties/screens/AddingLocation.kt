package com.example.kaaproperties.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.MainActivity
import com.example.kaaproperties.logic.states

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddingLocation(
    onEvent: (Events) -> Unit,
    states: states,
    navController: NavController,
    context: Context
) {
    AlertDialog(
        onDismissRequest = {
            onEvent(Events.NotAdding)
            navController.popBackStack()
            navController.navigate(route = "location_screen")
        },
        title = { Text(text = "Add Location")},
        text = {
               Column(verticalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.padding(10.dp)) {
                   TextField(
                       value = states.locationName,
                       onValueChange = {
                           onEvent(
                               Events.setLocationName(it)
                           )
                       },
                       label = { Text(text = "Enter Location Name") },
                       placeholder = { Text(text = "LocationName")}
                   )
                   TextField(
                       value = states.locationDescription,
                       onValueChange = {
                           onEvent(
                               Events.setLocationDescription(it)
                           )
                       },
                       label = { Text(text = "Enter Location Description") },
                       placeholder = { Text(text = "LocationDescription")}
                   )
               }
        },
        confirmButton = {
            Row(modifier = Modifier.fillMaxWidth().padding(5.dp), verticalAlignment = Alignment.Bottom,) {
                Button(onClick = {
                    navController.popBackStack()
                    onEvent(Events.NotAdding)
                    navController.navigate(route = "location_screen")
                }) {
                    Text(text = "Dismiss")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    onEvent(Events.saveLocation)
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Text("Save Location")
                }
            }
        }
    )
}