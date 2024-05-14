package com.example.kaaproperties.screens.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.ImagesList
import com.example.kaaproperties.logic.ListOfImages
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

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
    id: String,
    collectionPath: String,
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
            .height(200.dp),
        shape = RectangleShape,
        border = BorderStroke(0.1.dp, Color.Black),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(4f)
                    .padding(vertical = 15.dp, horizontal = 15.dp)
            ) {
                val images = retrieveImagesFromFireStore(id = id, collectionPath = collectionPath)
                val imageShown = if (images.listOfImages.isEmpty()){""}else{ images.listOfImages[0]}
                AsyncImage(
                    model = imageShown,
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.loading),
                    error = painterResource(id = errorId)
                )

            }
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = text1,
                        modifier = Modifier
                            .padding(5.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold

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
                Column {

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



@Composable
fun retrieveImagesFromFireStore(id: String, collectionPath: String): ListOfImages {

    val db = FirebaseFirestore.getInstance()
    var ListOfImages by remember {
        mutableStateOf(ListOfImages())
    }


    if (!id.isEmpty()){
        val imageRef = db.collection(collectionPath).document(id)
        imageRef.get()
            .addOnSuccessListener {
                val listOfImages = it.toObject<ListOfImages>()
                if (listOfImages != null) {
                    ListOfImages = listOfImages
                }
            }
    }
    return ListOfImages

}






