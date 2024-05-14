@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package com.example.kaaproperties.screens.tenants

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.screens.components.customButton
import com.example.kaaproperties.screens.components.customDailog
import com.example.kaaproperties.screens.components.customTextField2

@Composable
fun AddingTenants(
    state: states,
    onEvent: (Events) -> Unit,
    propertyId: String,
    navController: NavController,
    context: Context,
) {
    val _propertyId = propertyId.toInt()
    onEvent(Events.setpropertyId(_propertyId))

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    customDailog(
        onDismiss = {
            onEvent(Events.NotAdding)
            navController.navigate(Screens.Tenants.withArgs(propertyId)) {
                navController.popBackStack()
                Log.d("isStackPopped", "${navController.popBackStack()}")
            }
        },
        headerText = "Add Tenant",
        image = {
            val launcher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
                    selectedImageUri = uri
                }
            if (selectedImageUri == null) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            launcher.launch("image/*")
                        }
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = selectedImageUri
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            launcher.launch("image/*")
                        },
                    contentScale = ContentScale.Crop
                )
            }
        },
        content = {
            customTextField2(
                value = state.fullName,
                onValueChange = {
                    onEvent(
                        Events.setfullName(it)
                    )
                },
                placeHolder = "Tenant Name",
                iconId = null
            )
            customTextField2(
                value = state.email,
                onValueChange = {
                    onEvent(
                        Events.setemail(it)
                    )
                },
                placeHolder = "tenant@gmail.com",
                iconId = null
            )
            customTextField2(
                value = state.phoneNumber,
                onValueChange = {
                    onEvent(
                        Events.setphoneNumber(it)
                    )
                },
                placeHolder = "0712345678",
                iconId = null
            )
        },
        imageUriList = {},
        saveButton = {
            customButton(
                onClick = {
                    selectedImageUri?.let { Events.saveTenant(it) }?.let { onEvent(it) }
                    navController.navigate(Screens.Tenants.withArgs(propertyId)) {
                        navController.popBackStack()
                        Log.d("isStackPopped", "${navController.popBackStack()}")
                    }
                    onEvent(Events.NotAdding)
                },
                buttonText = "Save Tenant",
                iconId = R.drawable.baseline_save_24,
                color = 0xDA309FFF
            )
        }
    )

}
