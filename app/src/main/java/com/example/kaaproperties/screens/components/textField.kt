package com.example.kaaproperties.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kaaproperties.Authentication.email.isPasswordValid
import com.example.kaaproperties.PropertyViewModel
import com.example.kaaproperties.R
import com.example.kaaproperties.ui.theme.AlegreyoSansFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun customTextField(
    value: String = "",
    placeHolder: String = "",
    onValueChange: (String) -> Unit,
    iconId: Int = R.drawable.ic_email,
    viewModel: PropertyViewModel,
    isError: Boolean = false,
    containerColor: Color = Color.Transparent,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = Color.White
            )
        },
        isError = isError,
        enabled = !viewModel.loading.value,
        placeholder = {
            Text(
                text = placeHolder, style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = AlegreyoSansFontFamily,
                    color = Color(0xFFBEC2C2)
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            focusedIndicatorColor = Color.White,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )

    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun customPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    viewModel: PropertyViewModel,
    keyboardType: KeyboardType = KeyboardType.Password
) {
    var showPassword by remember {
        mutableStateOf(false)
    }
    var isPasswordError by remember {
        mutableStateOf(true)
    }

    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            isPasswordError = isPasswordValid(it)
        },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_password),
                contentDescription = "Password",
                tint = Color.White
            )
        },
//            PassWordVisibilityToggleIcon(showPassword = showPassword, onTogglePasswordVisibility = { showPassword = !showPassword})
        trailingIcon = {
            val image = if (!showPassword) {
                Icons.Filled.VisibilityOff
            } else {
                Icons.Filled.Visibility
            }
            val contentDescription = if (showPassword) {
                "Hide password icon"
            } else {
                "Show password icon"
            }
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(imageVector = image, contentDescription = contentDescription)
            }
        },
        isError = !isPasswordError,
        supportingText = {
            if (isPasswordError) {
                Text(
                    text = "Check your password",
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        enabled = !viewModel.loading.value,
        placeholder = {
            Text(
                text = "Password", style = TextStyle(
                    fontSize = 18.sp, fontFamily = AlegreyoSansFontFamily, color = Color(0xFFBEC2C2)
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),

        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            focusedIndicatorColor = Color.White,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun customTextField2(
    value: String = "",
    placeHolder: String = "",
    onValueChange: (String) -> Unit,
    iconId: Int?,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    containerColor: Color = Color(0xDA5F8BB1),
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        leadingIcon = {
            iconId?.let { painterResource(id = it) }?.let {
                Icon(
                    painter = it,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        isError = isError,
        enabled = isEnabled,
        placeholder = {
            Text(
                text = placeHolder, style = TextStyle(
                    fontSize = 18.sp, fontFamily = AlegreyoSansFontFamily, color = Color(0xFFBEC2C2)
                )
            )
        },
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 15.dp)
            .fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = containerColor,
            focusedContainerColor = containerColor,
            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            focusedIndicatorColor = Color.White,
        ),
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )


    )

}
