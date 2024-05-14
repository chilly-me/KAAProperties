package com.example.kaaproperties.screens.property

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.MainActivity
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.screens.components.customButton
import com.example.kaaproperties.screens.components.customDailog
import com.example.kaaproperties.screens.components.customTextField
import com.example.kaaproperties.screens.components.customTextField2
import com.example.kaaproperties.ui.theme.AlegreyoSansFontFamily

@Composable
fun AddingProperty3(
    state: states,
    onEvent: (Events) -> Unit,
    navController: NavController,
    locationId: String,
    context: Context,
) {
    Log.d("addingproperty", "location id = $locationId")

    val _locationId = locationId.toInt()
    onEvent(Events.setlocationId(_locationId))

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
            navController.navigate(Screens.Property.withArgs(locationId)) {
                navController.popBackStack()
                Log.d("isStackPopped", "${navController.popBackStack()}")
            }
        },
        headerText = "Add Property",
        image = {
            if (!ImageUriList.isEmpty()) {
                Row {
                    repeat(ImageUriList.size) { index ->
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageUriList[index],
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    launcher.launch("image/*")
                                }
                                .size(36.dp),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.property2),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clickable {
                            launcher.launch("image/*")
                        }
                        .clip(RectangleShape),
                    contentScale = ContentScale.Crop,
                )
            }
        },
        content = {
            customTextField2(
                value = state.propertyName,
                onValueChange = {
                    onEvent(
                        Events.setPropertyName(it),
                    )
                },
                placeHolder = "Alpha Courts",
                iconId = null,

                )
            customTextField2(
                value = state.propertyAddress,
                onValueChange = {
                    onEvent(
                        Events.setpropertyAddress(it),
                    )
                },
                placeHolder = "43-30100 Eldoret",
                iconId = null,
            )
            customTextField2(
                value = state.propertyDescription,
                onValueChange = {
                    onEvent(
                        Events.setpropertyDescription(it),
                    )
                },
                placeHolder = "One Bedrooom",
                iconId = null,
            )

            customTextField2(
                value = state.capacity,
                onValueChange = {
                    onEvent(
                        Events.setcapacity(it),
                    )
                },
                placeHolder = "40",
                iconId = null,
            )
        },
        saveButton = {
            onEvent(Events.saveProperty)
            navController.navigate(Screens.Property.withArgs(locationId)) {
                navController.popBackStack()

            }
        }
    )

}

@Composable
fun AddingProperty(
    state: states,
    onEvent: (Events) -> Unit,
    locationId: String,
    navController: NavController,
    context: Context,
) {
    val _locationId = locationId.toInt()
    onEvent(Events.setlocationId(_locationId))

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var selectingImage by remember {
        mutableStateOf(false)
    }
    customDailog(
        onDismiss = {
            onEvent(Events.NotAdding)
            navController.navigate(Screens.Property.withArgs(locationId)) {
                navController.popBackStack()
                Log.d("isStackPopped", "${navController.popBackStack()}")
            }
        },
        headerText = "Add Property",
        image = {
            val launcher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
                    selectedImageUri = uri
                    selectedImageUri?.let {
                        context.contentResolver.takePersistableUriPermission(
                            it,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    }
                    onEvent(Events.setUri(uri.toString()))
                }
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            launcher.launch("image/*")
                        }
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.property2),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            launcher.launch("image/*")
                        }
                )
            }
        },
        content = {
            customTextField2(
                value = state.propertyName,
                onValueChange = {
                    onEvent(
                        Events.setPropertyName(it)
                    )
                },
                placeHolder = "Alpha Courts",
                iconId = null
            )
            customTextField2(
                value = state.propertyAddress,
                onValueChange = {
                    onEvent(
                        Events.setpropertyAddress(it)
                    )
                },
                placeHolder = "23-30100 Eldoret",
                iconId = null
            )
            customTextField2(
                value = state.propertyDescription,
                onValueChange = {
                    onEvent(
                        Events.setpropertyDescription(it)
                    )
                },
                placeHolder = "One bedroom",
                iconId = null
            )
        },
        saveButton = {
            customButton(
                onClick = {
                    onEvent(Events.saveProperty)
                    navController.navigate(Screens.Property.withArgs(locationId)) {
                        navController.popBackStack()
                        Log.d("isStackPopped", "${navController.popBackStack()}")
                    }
                    onEvent(Events.NotAdding)
                },
                buttonText = "Save Property",
                iconId = R.drawable.baseline_save_24,
                color = 0xDA309FFF
            )
        }
    )

}
