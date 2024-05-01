package com.example.kaaproperties.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kaaproperties.MainActivity
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.logic.states

@Composable
fun AddingTenants(state: states, onEvent: (Events) -> Unit, propertyId: String, navController: NavController, context: Context) {
    val _propertyId = propertyId.toInt()
    onEvent(Events.setpropertyId(_propertyId))
    AlertDialog(
        onDismissRequest = {
            navController.popBackStack()
            navController.navigate(Screens.Tenants.withArgs(propertyId))
            onEvent(Events.NotAdding)
        },
        title = { Text(text = "Add Tenant") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.padding(10.dp)) {
                TextField(
                    value = state.fullName,
                    onValueChange = {
                        onEvent(
                            Events.setfullName(it)
                        )
                    },
                    label = { Text(text = "Enter Tenant's full Name") },
                    placeholder = { Text(text = "Jesse Baraza") }
                )
                TextField(
                    value = state.email,
                    onValueChange = {
                        onEvent(
                            Events.setemail(it)
                        )
                    },
                    label = { Text(text = "Enter Tenant's email") },
                    placeholder = { Text(text = "somebody@gmail.com") }
                )
                TextField(
                    value = state.phoneNumber,
                    onValueChange = {
                        onEvent(
                            Events.setphoneNumber(it)
                        )
                    },
                    label = { Text(text = "Enter PhoneNumber") },
                    placeholder = { Text(text = "0712345678") }
                )
            }
        },
        confirmButton = {
            Row(modifier = Modifier.fillMaxWidth().padding(5.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(40.dp)) {
                Button(onClick = {
                    onEvent(Events.saveTenant)
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Text("Save Tenant")
                }
                Button(onClick = {
                    navController.popBackStack()
                    navController.navigate(Screens.Tenants.withArgs(propertyId))
                    onEvent(Events.NotAdding)
                }) {
                    Text(text = "Dismiss")
                }
            }
        }
    )
}