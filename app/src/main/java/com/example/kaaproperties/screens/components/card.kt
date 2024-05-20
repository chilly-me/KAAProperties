package com.example.kaaproperties.screens.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kaaproperties.R
import com.example.kaaproperties.room.entities.payments
import com.example.kaaproperties.room.entities.property
import com.example.kaaproperties.room.entities.tenant
import com.example.kaaproperties.ui.theme.AlegreyoSansFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun customCard(
    navigation: () -> Unit,
    onEvent: () -> Unit,
    onDeleteEvent: () -> Unit,
    text1: String = "",
    text2: String = "",
    text3: String = "",
    text4: String = "",
    price: String? = null,
    imageList: ArrayList<String>,
    errorId: Int
) {

    Card(
        onClick = {
            onEvent()
            navigation()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RectangleShape)
            .height(400.dp),
        shape = RectangleShape,
        border = BorderStroke(0.1.dp, Color.Black),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(4f)
                    .fillMaxSize()
                    .padding(vertical = 15.dp, horizontal = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                val images = imageList
                val imageShown = if (images.isEmpty()) " " else images[0]
                var loading by remember {
                    mutableStateOf(true)
                }
                if (loading){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        this@Column.AnimatedVisibility(visible = true) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .size(100.dp),
                                color = Color.Gray

                            )


                        }
                    }


                }
                AsyncImage(
                    model = imageShown,
                    contentDescription = null,
                    onSuccess = { loading = false },
                    onError = { loading = false },
                    error = painterResource(id = errorId),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RectangleShape),
                    contentScale = ContentScale.Crop
                )



            }
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = text1,
                        modifier = Modifier
                            .padding(5.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight(800)

                    )
                    Text(
                        text = text2,
                        modifier = Modifier
                            .padding(2.dp),
                        fontSize = 10.sp,

                        )

                    Text(
                        text = text3,
                        modifier = Modifier
                            .padding(2.dp),
                        fontSize = 10.sp,

                        )
                    Text(
                        text = text4,
                        modifier = Modifier
                            .padding(2.dp),
                        fontSize = 10.sp,

                        )


                }


            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(end = 10.dp, bottom = 5.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (price != null){
                        Box(
                            modifier = Modifier
                                .weight(1f),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(text = "Ksh. $price", fontFamily = AlegreyoSansFontFamily)

                        }
                    }
                    Box(
                        modifier = Modifier
                        .weight(1f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        IconButton(onClick = { onDeleteEvent()}) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_delete_24),
                                contentDescription = "Delete "
                            )
                        }

                    }

                }

            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun paymentCard(
    payment: payments,
    tenant: tenant,
    property: property,
    onCall: () -> Unit
) {

    val tenant = tenant
    val property = property
    val payment = payment
    var color by remember {
        mutableStateOf(Color.Transparent)
    }
    val deficit = 5000

    Log.d("totalDeficit", "$deficit")
    Log.d("totalDeficit", "${payment.amount}: amount paid")
    Log.d("totalDeficit", "${property.cost}: cost for property")
    if (deficit >= 100) {
        color = Color(0xFFEE4E4E)
    } else if (deficit == 0) {
        color = Color(0xFF17A007)
    }
    Card(
        onClick = {
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RectangleShape)
            .height(300.dp),
        shape = RectangleShape,
        border = BorderStroke(0.1.dp, Color.Black),
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
//                .weight(4f)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tenant Name:   ${tenant.fullName}",
                textDecoration = TextDecoration.Underline,
                fontSize = 25.sp,
                fontFamily = AlegreyoSansFontFamily,
                fontWeight = FontWeight(700),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Property Name:   ${property.propertyName}",
                textDecoration = TextDecoration.Underline,
                fontSize = 18.sp,
                fontFamily = AlegreyoSansFontFamily,
                fontWeight = FontWeight(500),
                color = Color.White

            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(30.dp)) {
                coluumnForPayments("Month", payment.month)
                coluumnForPayments("Cost", "Ksh. ${property.cost}")
                coluumnForPayments("Paid", "Ksh. ${payment.amount}")
            }
            Spacer(modifier = Modifier.weight(0.4f))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)){
                Box(modifier = Modifier
                    .weight(2f)
                    .fillMaxSize(),
                    contentAlignment = Alignment.CenterEnd)
                {
                    coluumnForPayments(text1 = "Total Deficit", text2 = "Ksh. $deficit")
                }
                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                    contentAlignment = Alignment.Center)
                {
                    IconButton(onClick = { onCall() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_phone),
                            contentDescription = "Call",
                            tint = Color.White
                        )
                    }
                }
            }

        }

    }
}

@Composable
fun coluumnForPayments(text1: String, text2: String) {
    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
        Text(
            text = text1,
            textDecoration = TextDecoration.Underline,
            fontSize = 18.sp,
            fontFamily = AlegreyoSansFontFamily,
            color = Color.White


        )
        Text(
            text = text2,
            fontSize = 18.sp,
            fontFamily = AlegreyoSansFontFamily,
            textAlign = TextAlign.Center,
            color = Color.White


        )
    }
}
