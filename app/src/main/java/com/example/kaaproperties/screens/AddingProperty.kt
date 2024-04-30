package com.example.kaaproperties.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kaaproperties.Events
import com.example.kaaproperties.room.entities.states

@Composable
fun AddingProperty(state: states, onEvent: (Events) -> Unit, LocationId: Int) {
    onEvent(Events.setlocationId(LocationId))
    AlertDialog(
        onDismissRequest = { onEvent(Events.NotAdding) },
        title = { Text(text = "Add Property") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.padding(10.dp)) {
                TextField(
                    value = state.propertyName,
                    onValueChange = {
                        onEvent(
                            Events.setPropertyName(it)
                        )
                    },
                    label = { Text(text = "Enter Property Name") },
                    placeholder = { Text(text = "Alpha Courts") }
                )
                TextField(
                    value = state.propertyAddress,
                    onValueChange = {
                        onEvent(
                            Events.setpropertyAddress(it)
                        )
                    },
                    label = { Text(text = "Enter Property Address") },
                    placeholder = { Text(text = "43-30100 Eldoret") }
                )
                TextField(
                    value = state.propertyDescription,
                    onValueChange = {
                        onEvent(
                            Events.setpropertyDescription(it)
                        )
                    },
                    label = { Text(text = "Enter Property Description") },
                    placeholder = { Text(text = "One Bedrooom") }
                )
                TextField(
                    value = state.capacity,
                    onValueChange = {
                        onEvent(
                            Events.setcapacity(it)
                        )
                    },
                    label = { Text(text = "Enter Property Capacity") },
                    placeholder = { Text(text = "40") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                )
            }
        },
        confirmButton = {
            Row(modifier = Modifier.fillMaxWidth().padding(5.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(40.dp)) {
                Button(onClick = {onEvent(Events.saveProperty)}) {
                    Text("Save Location")
                }
                Button(onClick = { onEvent(Events.NotAdding) }) {
                    Text(text = "Dismisss")
                }
            }
        }
    )
}