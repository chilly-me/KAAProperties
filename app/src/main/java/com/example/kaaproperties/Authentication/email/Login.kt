package com.example.kaaproperties.Authentication.email

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kaaproperties.AuthenticationViewModel
import com.example.kaaproperties.MainActivity
import com.example.kaaproperties.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

private lateinit var auth: FirebaseAuth

@SuppressLint("StaticFieldLeak")
private lateinit var firebaseFirestore: FirebaseFirestore
var isLoading: Boolean = false
@Composable
fun LoginLayout(
    onLogin: (email: String, password: String) -> Unit, navController: NavController, viewModel: AuthenticationViewModel
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

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login To Your Account",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = email,
            leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_email), contentDescription = "Email")},
            onValueChange = { email = it },
            label = { Text("Email") },
            enabled = !viewModel.loading.value
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = password,
            onValueChange = {
                password = it
                isPasswordError = isPasswordValid(it)
            },
            leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_password), contentDescription = "Password")},
            visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation(),
//            PassWordVisibilityToggleIcon(showPassword = showPassword, onTogglePasswordVisibility = { showPassword = !showPassword})
            trailingIcon = {
                val image = if(!showPassword){
                    Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Visibility
                }
                val contentDescription = if (showPassword){
                    "Hide password icon"
                } else{
                    "Show password icon"
                }
                IconButton(onClick = { showPassword = !showPassword }){
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
            label = { Text("Password") },
            enabled = !viewModel.loading.value
        )
        Spacer(modifier = Modifier.height(10.dp))


        AnimatedVisibility(visible = viewModel.loading.value) {
            CircularProgressIndicator(
                modifier = Modifier.padding(20.dp)
            )

        }

        Button(
            onClick = {
            onLogin(email, password)
        },
            enabled = !viewModel.loading.value
        ) {
            Text(text = "Login")
        }


        Text(
            text = "Don't have an account create one here",
            if (!viewModel.loading.value){
                Modifier
                    .clickable { navController.navigate(route = "sign_up_screen")
                }
            }else{
                 Modifier
            }
            ,

            )

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
    modifier: Modifier = Modifier.fillMaxSize(), navController: NavController, context: Context, viewModel: AuthenticationViewModel
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


fun LoginUser(email: String, password: String, context: Context, navController: NavController, viewModel: AuthenticationViewModel) {
    auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authenticatingTask ->
        isLoading = false
        if (authenticatingTask.isSuccessful) {
            viewModel.isNotLoading()
            Toast.makeText(
                context, "You have logged into your account successfully", Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        } else {
            Toast.makeText(context, authenticatingTask.exception?.message, Toast.LENGTH_SHORT)
                .show()
            viewModel.isNotLoading()
        }
    }

}
