package com.example.kaaproperties.screens.tenants

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.room.entities.payments
import com.example.kaaproperties.screens.components.customButton
import com.example.kaaproperties.screens.components.customScaffold
import com.example.kaaproperties.screens.user.customRow
import com.example.kaaproperties.ui.theme.AlegreyoSansFontFamily

@Composable
fun Payments(
    navController: NavController,
    onEvents: (Events) -> Unit,
    states: states,
    context: Context,
    propertyId:String,
    propertyName: String,
    cost: String
) {
    customScaffold(
        navController = navController,
        onEvents = onEvents,
        states = states,
        ifAdding = { /*TODO*/ },
        titleText = states.selectedTenant.fullName,
        titleIcon = R.drawable.baseline_attach_money_24,
        floatingActionButtonId = null,
        screen = {
            val tenant = states.selectedTenant
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                var loading by remember {
                    mutableStateOf(true)
                }
                if (loading) {
                    Box(
                        modifier = Modifier
                            .size(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        this@Column.AnimatedVisibility(visible = true) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .size(80.dp),
                                color = Color.Gray

                            )
                        }
                    }
                }
                if (tenant.Imageuri != null) {
                    AsyncImage(
                        model = tenant.Imageuri,
                        contentDescription = null,
                        onSuccess = { loading = false },
                        onError = { loading = false },
                        error = painterResource(id = R.drawable.profile),
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile",
                        Modifier
                            .aspectRatio(2f)
//                            .size(40.dp)
                    )
                }
                Text(
                    text = tenant.fullName,
                    fontSize = 24.sp,
                    fontFamily = AlegreyoSansFontFamily,
                    fontWeight = FontWeight(800),
                    modifier = Modifier
                        .padding(vertical = 40.dp)

                )
                Text(
                    text = "Contacts",
                    textDecoration = TextDecoration.Underline,
                    fontSize = 24.sp,
                    fontFamily = AlegreyoSansFontFamily,
                    fontWeight = FontWeight(800),
                    modifier = Modifier

                )
                customRow(
                    imgId = R.drawable.ic_email,
                    contentDescription = "Email",
                    text = tenant.email,
                    clickIcon = {
                        SendingEmails(
                            email = tenant.email,
                            hasPaid = tenant.hasPaid,
                            name = tenant.fullName,
                            context = context
                        )
                    }
                )
                customRow(
                    imgId = R.drawable.ic_phone,
                    contentDescription = "Phone Number",
                    text = tenant.phoneNumber,
                    clickIcon = {
                        val u = Uri.parse("tel:" + tenant.phoneNumber)
                        val i = Intent(Intent.ACTION_DIAL, u)
                        try {
                            context.startActivity(i)
                        } catch (s: SecurityException) {
                            Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                )
                SimpleTabe(paymentDeatils = states.payments, cost = states.propertyCost, tenant_Name = tenant.fullName)
                Spacer(modifier = Modifier.weight(0.2f))
                customButton(
                    onClick = {
                        onEvents(Events.deleteTenant(tenant))
                        navController.navigate(Screens.Tenants.withArgs(propertyId))
                    },
                    buttonText = "Delete Tenant",
                    iconId = R.drawable.baseline_delete_24,
                    color = 0xFFFA2323,
                )

            }
        }
    )
}

@Composable
fun RowScope.TableCell(text: String = "", weight: Float = 0.5f) {
    Text(
        text = text,
        modifier = Modifier
            .border(1.dp, Color.White)
            .weight(weight)
            .padding(8.dp)
    )
}

@Composable
fun SimpleTabe(paymentDeatils: List<payments>, cost: String, tenant_Name: String) {
    val column1Weight = .3f
    val columnWeightForTheRest = .2f
    Column(
        Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                Modifier
                    .background(Color.Gray)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                TableCell(text = "Tenant Name: $tenant_Name", weight = column1Weight)

            }
            Row(Modifier.background(Color.Gray)) {
                TableCell(text = "Month", weight = columnWeightForTheRest)
                TableCell(text = "Paid", weight = columnWeightForTheRest)
                TableCell(text = "Cost", weight = columnWeightForTheRest)
                TableCell(text = "Deficit", weight = columnWeightForTheRest)
            }
        }


        paymentDeatils.forEach {
            val deficit = cost.toInt() - it.amount.toInt()
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = it.month, weight = columnWeightForTheRest)
                TableCell(text = it.amount, weight = columnWeightForTheRest)
                TableCell(text = "Ksh. $cost", weight = columnWeightForTheRest)
                TableCell(text = "Ksh. $deficit", weight = columnWeightForTheRest)
            }
        }
    }
}

@Composable
fun makePayment() {

}