package com.example.kaaproperties.screens.tenants

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
import com.example.kaaproperties.room.entities.property
import com.example.kaaproperties.room.entities.tenant
import com.example.kaaproperties.screens.components.customButton
import com.example.kaaproperties.screens.components.customCard
import com.example.kaaproperties.screens.components.customDailog
import com.example.kaaproperties.screens.components.customExposedDropdownMenuSample
import com.example.kaaproperties.screens.components.customScaffold
import com.example.kaaproperties.screens.components.customTextField2
import com.example.kaaproperties.screens.user.customRow
import com.example.kaaproperties.ui.theme.AlegreyoSansFontFamily

@Composable
fun Payments(
    navController: NavController,
    onEvents: (Events) -> Unit,
    states: states,
    context: Context,
) {
    var makingPayments by remember {
        mutableStateOf(false)
    }
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
            if (makingPayments) {
                MakePayment(
                    states = states,
                    onEvents = onEvents,
                    onDismiss = { makingPayments = false }
                )
            }
            Log.d("tenanId", "${tenant.tenantId}")

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
                SimpleTabe(
                    paymentDeatils = states.payments,
                    cost = states.selectedProperty.cost,
                    tenant = tenant
                )
                Spacer(modifier = Modifier.weight(0.2f))

                customButton(
                    onClick = {
                        makingPayments = !makingPayments
                    },
                    buttonText = "Make payment",
                    iconId = R.drawable.baseline_attach_money_24,
                    color = 0xFF17A007,
                )
                Spacer(modifier = Modifier.weight(0.2f))
                customButton(
                    onClick = {
                        onEvents(Events.deleteTenant(tenant))
                        navController.navigate(Screens.Tenants.route)
                    },
                    buttonText = "Delete Tenant",
                    iconId = R.drawable.baseline_delete_24,
                    color = 0xFFEE4E4E,
                )


            }
        }
    )
}

@Composable
fun RowScope.TableCell(text: String? = "", weight: Float = 0.5f) {
    if (text != null) {
        Text(
            text = text,
            modifier = Modifier
                .border(1.dp, Color.White)
                .weight(weight)
                .padding(8.dp),
            fontFamily = AlegreyoSansFontFamily,
            fontSize = 15.sp,
            color = Color.White
        )
    }
}

@Composable
fun SimpleTabe(
    paymentDeatils: List<payments>,
    cost: String,
    tenant: tenant,
    context: Context = LocalContext.current,
) {
    val column1Weight = 1f
    val columnWeightForTheRest = .2f
    Column(
        Modifier
            .fillMaxSize()
            .padding(1.dp)
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TableCell(text = "Tenant Name: ${tenant.fullName}", weight = column1Weight)

            }
            Row(Modifier.background(Color.Transparent)) {
                TableCell(text = "Month", weight = 0.3f)
                TableCell(text = "Paid", weight = columnWeightForTheRest)
                TableCell(text = "Cost", weight = columnWeightForTheRest)
                TableCell(text = "Deficit", weight = columnWeightForTheRest)
                TableCell(text = "Email", weight = columnWeightForTheRest)
            }
        }


        paymentDeatils.forEach {
            val deficit = cost.toInt() - it.amount.toInt()
            var color by remember {
                mutableStateOf(Color.Transparent)
            }
            var positiveReview by remember {
                mutableStateOf(false)
            }
            if (deficit >= 100) {
                color = Color(0xFFEE4E4E)
                positiveReview = false
            } else if (deficit == 0) {
                color = Color(0xFF17A007)
                positiveReview = true
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(color)
            ) {

                TableCell(text = it.month, weight = .3f)
                TableCell(text = it.amount, weight = columnWeightForTheRest)
                TableCell(text = cost, weight = columnWeightForTheRest)
                TableCell(text = "$deficit", weight = columnWeightForTheRest)
                IconButton(
                    onClick = {
                        SendingEmails(
                            email = tenant.email,
                            positiveReview = positiveReview,
                            name = tenant.fullName,
                            context = context,
                            month = it.month
                        )
                    },
                    modifier = Modifier
                        .weight(columnWeightForTheRest)
                        .border(1.dp, Color.White)
                        .height(43.dp)
                    ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_email),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun MakePayment(states: states, onEvents: (Events) -> Unit, onDismiss: () -> Unit) {
    val property = states.selectedProperty
    val tenant = states.selectedTenant
    Log.d("tenanId", "${tenant.tenantId}")
    customDailog(
        onDismiss = { onDismiss() },
        headerText = "Payment by ${tenant.fullName}",
        content = {
            val options = listOf(
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
            )

            customExposedDropdownMenuSample(
                options = options,
                value = {
                    onEvents(Events.setMonth(it))
                    Log.d("month", it + " in paymentScreen")

                },
                iconId = R.drawable.baseline_calendar_month_24
            )
            customTextField2(
                value = states.amount,
                onValueChange = { onEvents(Events.setAmount(it)) },
                iconId = null,
                keyboardType = KeyboardType.Number,
                placeHolder = "Ksh. 1000"
            )
        },
        saveButton = {
            customButton(
                onClick = {
                    onEvents(Events.setTenantId(tenant.tenantId))
                    onEvents(Events.setpropertyId(property.propertyId))
                    Log.d("tenanId", "${tenant.tenantId}")
                    onEvents(Events.savePayment)
                    onDismiss()
                },
                buttonText = "Make Payment",
                iconId = R.drawable.baseline_attach_money_24,
                color = 0xFF17A007,

                )

        },
        imageUriList = null,

        )
}
