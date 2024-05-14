package com.example.kaaproperties.screens.tenants

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.transition.Transition
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.kaaproperties.MainActivity
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.screens.components.customScaffold

@Composable
fun scaffoldForTenants(
    navController: NavController,
    onEvents: (Events) -> Unit,
    states: states,
    propertyId: String,
    context: Context,
) {
    customScaffold(
        navController = navController,
        onEvents = onEvents,
        states = states,
        ifAdding = { /*TODO*/ },
        titleText = "Tenants in ${states.propertyName}",
        titleIcon = R.drawable.ic_groups,

    ) {
        Column(modifier = Modifier) {
            if (states.isAdding) {
                navController.navigate(Screens.AddingTenants.withArgs(propertyId))
            }
            Tenants(
                onEvent = onEvents,
                state = states,
                navController = navController,
                propertyId = propertyId,
                context = context
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tenants(
    state: states,
    onEvent: (Events) -> Unit,
    navController: NavController,
    propertyId: String,
    context: Context,
) {

    LazyColumn {
        items(state.tenants) { tenant ->
            Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(40.dp)) {
                var selected = remember {
                    mutableStateOf(false)
                }
                var image by remember {
                    mutableStateOf<Drawable?>(null)
                }
                Card(
                    onClick = {
                        selected.value = !selected.value
                        onEvent(Events.selectTenant(tenant.tenantId))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(100.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                    ) {


                        Box(modifier = Modifier.weight(2f)) {


                            if (tenant.uri != null) {
                                Log.d("ProfileImageURI", tenant.uri + " in string")
                                val imageUri = Uri.parse(tenant.uri)
                                Log.d("ProfileImageURI", "${tenant.uri} actual uri")


                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.profile),
                                    contentDescription = "Real Estate 1",
                                    Modifier
                                        .aspectRatio(2f)
//                            .size(40.dp)
                                )
                            }

                        }
                        Box(modifier = Modifier.weight(2f)) {
                            Column {
                                Text(
                                    text = tenant.fullName,
                                    modifier = Modifier
                                        .padding(5.dp),
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
                        if (tenant.hasPaid) {
                            val colour = Color.Green
                            Box(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .background(colour), contentAlignment = Alignment.TopCenter
                            ) {
                                Text(text = "")
                            }
                        } else {
                            val colour = Color.Red
                            Box(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .background(colour), contentAlignment = Alignment.TopCenter
                            ) {
                                Text(text = "")

                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 10.dp), contentAlignment = Alignment.CenterEnd
                        ) {
                            Column {


                                IconButton(onClick = {
                                    val u = Uri.parse("tel:" + tenant.phoneNumber)
                                    val i = Intent(Intent.ACTION_DIAL, u)
                                    try {
                                        context.startActivity(i)
                                    } catch (s: SecurityException){
                                        Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG)
                                            .show()
                                    }
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_phone),
                                        contentDescription = "Paid Rent"
                                    )
                                }
                                if (selected.value) {
                                    PayingRent(
                                        onEvents = onEvent,
                                        context = context,
                                        navController = navController,
                                        states = state
                                    )
                                    Log.d("TenantName", "tenanatName: ${tenant.fullName}")
                                }
                                IconButton(onClick = {
                                    SendingEmails(
                                        email = tenant.email,
                                        hasPaid = tenant.hasPaid,
                                        name = tenant.fullName,
                                        context = context
                                    )
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_email),
                                        contentDescription = "Send Email"
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

fun SendingEmails(email: String, hasPaid: Boolean, name: String, context: Context) {
    val intent = Intent(Intent.ACTION_SEND)

    val emailAddress = arrayOf(email)

    intent.setType("message/rfc822")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
    if (hasPaid) {
        intent.putExtra(Intent.EXTRA_SUBJECT, "Appreciation for your rent payment")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Kaa Properties thanks you $name for paying your rent in time. We look forward to better times ahead together"
        )
    } else {
        intent.putExtra(Intent.EXTRA_SUBJECT, "Reminder for your rent")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Kaa Properties reminds you $name to pay your rent before the end of the month."
        )
    }
    context.startActivity(Intent.createChooser(intent, "Choose an Email Client"))

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayingRent(
    onEvents: (Events) -> Unit,
    context: Context,
    navController: NavController,
    states: states,
) {
    val tenant = states.selectedtenant
    AlertDialog(
        onDismissRequest = { navController.navigate(Screens.Tenants.withArgs(states.propertyId.toString())) },
        title = { Text(text = "Has ${tenant.fullName} paid rent") },
        confirmButton = {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Button(onClick = {
                    onEvents(Events.confirmRent(hasPaid = true, tenantId = tenant.tenantId))
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Text(text = "Has Paid Rent")
                }
                Button(onClick = {
                    onEvents(Events.confirmRent(hasPaid = false, tenantId = tenant.tenantId))
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Text(text = "Has Not Paid Rent")
                }
                Button(onClick = { navController.navigate(Screens.Tenants.withArgs(states.propertyId.toString())) }) {
                    Text(text = "Dismiss")

                }
            }
        },
        modifier = Modifier
            .height(400.dp)
            .width(350.dp)
    )
}