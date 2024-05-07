package com.example.kaaproperties.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.ImagesList

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
    type: String = "",
) {
    Card(
        onClick = {
            onEvent()
            navigation()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        var expanded = remember{
            mutableStateOf(false)
        }
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)) {
            Box (modifier = Modifier.weight(2f)){
                val images = ImagesList().imagesforproperty.random().imageId
                Image(
                    painter = painterResource(id = images),
                    contentDescription = "Real Estate 1",
                    Modifier
                        .aspectRatio(2f)
//                            .size(40.dp)
                )

            }
            Box(modifier = Modifier.weight(2f)) {
                Column {
                    Text(
                        text = text1,
                        modifier = Modifier
                            .padding(12.dp),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold

                    )
                    Text(
                        text = text2 + " " + text3,
                        modifier = Modifier
                            .padding(12.dp),
                        fontSize = 10.sp,

                        )
                    Text(
                        text = text4,
                        modifier = Modifier
                            .padding(12.dp),
                        fontSize = 10.sp,

                        )

                }


            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd){
                Column {
                    IconButton(onClick = { expanded.value = !expanded.value }) {
                        if (expanded.value){
                            Icon(painter = painterResource(id = R.drawable.ic_show_less), contentDescription = "Show Less")
                        }else{
                            Icon(painter = painterResource(id = R.drawable.ic_show_more), contentDescription = "Show More")
                        }
                    }
                    IconButton(onClick = { onDeleteEvent() }) {
                        Icon(painter = painterResource(id = R.drawable.baseline_delete_24), contentDescription = "Delete $type")
                    }

                }

            }



        }

    }
}