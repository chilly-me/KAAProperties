package com.example.kaaproperties.screens.location

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.screens.components.customButton
import com.example.kaaproperties.screens.components.customDailog
import com.example.kaaproperties.screens.components.customTextField2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddingLocation(
    onEvent: (Events) -> Unit,
    states: states,
    navController: NavController,
    context: Context,
) {
    var ImageUriList by remember {
        mutableStateOf<List<Uri?>>(emptyList())
    }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) { uriList ->
            ImageUriList = uriList
        }
    customDailog(
        onDismiss = {
            onEvent(Events.NotAdding)
            navController.navigate(Screens.Locations.route) {
                navController.popBackStack()
                Log.d("isStackPopped", "${navController.popBackStack()}")
            }

        },
        headerText = "Add Location",
        image = {
            if (!ImageUriList.isEmpty()) {
                Row {
                    repeat(ImageUriList.size) { index ->
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageUriList[index]
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    launcher.launch("image/*")
                                }
                                .size(36.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.apartment10),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clickable {
                            launcher.launch("image/*")
                        }
                        .clip(RectangleShape),
                    contentScale = ContentScale.Crop
                )
            }
        },
        content = {
            customTextField2(
                value = states.locationName,
                onValueChange = { onEvent(
                    Events.setLocationName(it)
                ) },
                placeHolder = "Location Name",
                iconId = R.drawable.baseline_location_on_24
            )
            customTextField2(
                value = states.locationDescription,
                onValueChange = {
                    onEvent(
                        Events.setLocationDescription(it)
                    )
                },
                placeHolder = "Location Description",
                iconId = null,
                singleLine = false
            )
        },
        saveButton = {
            customButton(
                onClick = {
                    onEvent(Events.saveLocation(ImageUriList))
                    navController.navigate(Screens.Locations.route) {
                        navController.popBackStack()
                        android.util.Log.d("isStackPopped", "${navController.popBackStack()}")
                    }
                },
                buttonText = "Save Location",
                iconId = R.drawable.baseline_save_24,
                color = 0xDA309FFF
            )
        },
    )

}