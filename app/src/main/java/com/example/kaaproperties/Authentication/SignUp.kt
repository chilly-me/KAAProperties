package com.example.kaaproperties.Authentication

import android.content.Context
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.kaaproperties.R
import kotlinx.coroutines.delay
import java.io.Serializable

@Composable
fun RegistrationUI(
    OnRegister: (email: String, password: String, username: String, profilePic: Uri?, address: String, age: String) -> Unit,
    context: Context = LocalContext.current
) {
    var email by remember {
    mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var username by remember {
        mutableStateOf("")
    }
    var address by remember {
        mutableStateOf("")
    }
    var age by remember {
        mutableStateOf("")
    }
    var profilePic by remember {
        mutableStateOf<Uri?>(null)
    }
    var isPasswordValid by remember {
        mutableStateOf<Boolean>(true)
    }


    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImagePicker(onCalled = { selectedImage: Uri -> profilePic = selectedImage })

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Username") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Username") }
        )
        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            val passwordValidation = validatePassword(password, context)
            if ( passwordValidation && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                OnRegister(email,password,username,profilePic,address,age )
            }else{
                isPasswordValid = false
            }
            }
        ) {

        }
        if(!isPasswordValid){
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Check your email address", Toast.LENGTH_SHORT).show()
            }
        }
    }


}

fun validatePassword(pwd: String, context: Context): Boolean{
    val ERR_LEN = "Password must have at least eight characters!"
    val ERR_WHITESPACE = "Password must not contain whitespace!"
    val ERR_DIGIT = "Password must contain at least one digit!"
    val ERR_UPPER = "Password must have at least one uppercase letter!"
    val ERR_SPECIAL = "Password must have at least one special character, such as: _%-=+#@"

    if (pwd.length >=8){
        Toast.makeText(context, ERR_LEN, Toast.LENGTH_SHORT).show()
        return false
    }else if(pwd.none{it.isWhitespace()}){
        Toast.makeText(context, ERR_WHITESPACE, Toast.LENGTH_SHORT).show()
        return false
    }else if(pwd.any { it.isDigit() }){
        Toast.makeText(context, ERR_DIGIT, Toast.LENGTH_SHORT).show()
        return false
    }else if(pwd.any { it.isUpperCase() }){
        Toast.makeText(context, ERR_UPPER, Toast.LENGTH_SHORT).show()
        return false
    }else if(pwd.any { !it.isLetterOrDigit() }){
        Toast.makeText(context, ERR_SPECIAL, Toast.LENGTH_SHORT).show()
        return false
    }else {
        return true
    }
}

@Composable
fun ImagePicker(onCalled: (selectedImage: Uri) -> Unit){
    var selectedImage by remember {
        mutableStateOf<Uri?>(null)
    }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        selectedImage = it
    }

    if (selectedImage == null) {
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Profile Image",
            Modifier
                .clip(CircleShape)
                .size(150.dp)
                .clickable {
                    launcher.launch("image/*")
                }
        )
    }else{
        Image(
            painter = rememberAsyncImagePainter(selectedImage),
            contentDescription = "Profile Image",
            Modifier
                .clip(CircleShape)
                .size(150.dp)
                .clickable {
                    launcher.launch("image/*")
                }
        )
    }
}


@Composable
fun Register(modifier: Modifier = Modifier.fillMaxSize(),navController: NavController) {
    RegistrationUI (OnRegister = {email: String, password: String, username: String, profilePic: Uri?, address: String, age: String ->  })

}