package com.example.kaaproperties.Authentication.email

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kaaproperties.PropertyViewModel
import com.example.kaaproperties.MainActivity
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.screens.components.customButton
import com.example.kaaproperties.screens.components.customPasswordTextField
import com.example.kaaproperties.screens.components.customTextField
import com.example.kaaproperties.ui.theme.AlegreyoSansFontFamily
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

private lateinit var auth: FirebaseAuth

@SuppressLint("StaticFieldLeak")
private lateinit var firebaseFirestore: FirebaseFirestore
var isLoading: Boolean = false
@Composable
fun LoginLayout(
    onLogin: (email: String, password: String) -> Unit, navController: NavController, viewModel: PropertyViewModel
) {
    var showPassword by remember {
        mutableStateOf(false)
    }
    var isPasswordError by remember {
        mutableStateOf(true)
    }

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.apartment14),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .height(190.dp)
                    .align(Alignment.BottomCenter),
                contentScale = ContentScale.FillBounds
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 54.dp)
                        .height(150.dp)
                        .align(Alignment.Start)
                        .offset(x = (-20).dp)
                        .border(width = 0.dp, color = Color.Transparent),
                    colorFilter = ColorFilter.tint(Color.White)
                )

                Text(
                    text = "Sign In",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontFamily = AlegreyoSansFontFamily,
                        color = Color(0xB2FFFFFF),
                        fontWeight = FontWeight(500)
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 24.dp)
                )
                Text(
                    text = "Sign In now to access our Services",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = AlegreyoSansFontFamily,
                        color = Color(0xB2FFFFFF)
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 24.dp)
                )
                customTextField(
                    value = email,
                    placeHolder = "Email Address",
                    onValueChange = {
                        email = it
                    },
                    viewModel = viewModel
                )
                customPasswordTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        isPasswordError = isPasswordValid(it)
                    },
                    viewModel = viewModel
                )
                AnimatedVisibility(visible = viewModel.loading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(20.dp)

                    )

                }
//                Spacer(modifier = Modifier.height(20.dp))
                customButton(
                    onClick = { onLogin(email, password) },
                    buttonText = "Login",
                    color = 0xDA309FFF,
                    iconId = null,
                    enabled = !viewModel.loading.value
                )

                Row(modifier = Modifier.padding(top = 12.dp, bottom = 52.dp))
                {
                    Text(
                        text = "Don't have an account?",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = AlegreyoSansFontFamily,
                            color = Color.White

                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Sign Up",
                        modifier = if (!viewModel.loading.value) {
                            Modifier
                                .clickable {
                                    navController.navigate(Screens.SignUp.route)
                                }
                        } else {
                            Modifier
                        },
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = AlegreyoSansFontFamily,
                            fontWeight = FontWeight(800),
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun PassWordVisibilityToggleIcon(
    showPassword: Boolean,
    onTogglePasswordVisibility: () -> Unit
) {
    val image = if(!showPassword){
        Icons.Filled.Visibility
    } else {
        Icons.Filled.VisibilityOff
    }
    val contentDescription = if (showPassword){
        "Hide password icon"
    } else{
        "Show password icon"
    }

    IconButton(onClick = { onTogglePasswordVisibility }) {
        Icon(imageVector = image, contentDescription = contentDescription)
    }

}

fun isPasswordValid(it: String): Boolean {
    val password = it
    val passwordRegex =Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@\$!%*?&])[A-Za-z\\\\d@\$!%*?&]{8,}\$")
    return password.matches((passwordRegex).toRegex())
}

@Composable
fun loginUser(
    modifier: Modifier = Modifier.fillMaxSize(), navController: NavController, context: Context, viewModel: PropertyViewModel
) {

    LoginLayout(
        onLogin = { email: String, password: String ->
            if (email.isEmpty()) {
                Toast.makeText(context, "Cannot login with empty email", Toast.LENGTH_SHORT).show()
            }else if (password.isEmpty()){
                Toast.makeText(context,"Check your password", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.isLoading()
                LoginUser(email, password, context, navController, viewModel)
            }
        }, navController, viewModel
    )
}


fun LoginUser(email: String, password: String, context: Context, navController: NavController, viewModel: PropertyViewModel) {
    auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authenticatingTask ->
        isLoading = false
        if (authenticatingTask.isSuccessful) {
            viewModel.isNotLoading()
            Toast.makeText(
                context, "You have logged into your account successfully", Toast.LENGTH_SHORT
            ).show()
            navController.navigate(Screens.Locations.route){
                navController.popBackStack()
            }
        } else {
            Toast.makeText(context, authenticatingTask.exception?.message, Toast.LENGTH_SHORT)
                .show()
            viewModel.isNotLoading()
        }
    }

}
