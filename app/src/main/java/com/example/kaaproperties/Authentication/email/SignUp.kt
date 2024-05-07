package com.example.kaaproperties.Authentication.email

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.kaaproperties.AuthenticationViewModel
import com.example.kaaproperties.MainActivity
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference



private lateinit var auth: FirebaseAuth
@SuppressLint("StaticFieldLeak")
private lateinit var firebaseFirestore: FirebaseFirestore
private lateinit var storageRef: StorageReference
@Composable
fun RegistrationUI(
    onRegister: (email: String, password: String, username: String, profilePic: Uri?, address: String, age: String) -> Unit,
    context: Context, viewModel: AuthenticationViewModel,navController: NavController
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create an Account",
            fontSize = 20.sp
        )
        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            profilePic = it
        }

        if (profilePic == null) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile Image",
                Modifier
                    .clip(CircleShape)
                    .size(200.dp)
                    .clickable {
                        launcher.launch("image/*")
                    }
            )
        }else{
            Image(
                painter = rememberAsyncImagePainter(profilePic),
                contentDescription = "Profile Image",
                Modifier
                    .clip(CircleShape)
                    .size(150.dp)
                    .clickable {
                        launcher.launch("image/*")
                    }
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_email), contentDescription = "Email")},
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
            visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_password), contentDescription = "Password")},
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
        TextField(
            value = username,
            onValueChange = { username = it },
            leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_username), contentDescription = "Username")},
            label = { Text("Username") },
            enabled = !viewModel.loading.value

        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = address,
            onValueChange = { address = it },
            leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_address), contentDescription = "Address")},
            label = { Text("Address") },
            enabled = !viewModel.loading.value

        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = age,
            onValueChange = { age = it },
            leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_age), contentDescription = "Age")},
            label = { Text("Age") },
            enabled = !viewModel.loading.value

        )
        Spacer(modifier = Modifier.height(11.dp))

        AnimatedVisibility(visible = viewModel.loading.value) {
            CircularProgressIndicator(
                modifier = Modifier.padding(20.dp)
            )

        }

        Button(onClick = {
            validatePassword(password, context)
            validateEmail(email, context)
            val passwordvalidation = validatePassword(password, context)
            val emailvalidation = validateEmail(email, context)
            if (passwordvalidation && emailvalidation){
                onRegister(
                    email, password, username, profilePic, address, age
                )
            }

        },
            enabled = !viewModel.loading.value
        ) {
            Text(text = "Register")
        }

        Text(
            text = "Already have an account? Login here",
            if (!viewModel.loading.value){
                Modifier
                    .clickable { navController.navigate(Screens.LoginScreen.route)
                    }
            }else{
                Modifier
            }

            )

    }


}

fun validateEmail(email: String, context: Context): Boolean{
    return if ( Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        true
    }else{
        Toast.makeText(context, "Check your email Address", Toast.LENGTH_SHORT).show()
        false
    }

}
fun validatePassword(pwd: String, context: Context): Boolean{
    val ERR_LEN = "Password must have at least eight characters!"
    val ERR_WHITESPACE = "Password must not contain whitespace!"
    val ERR_DIGIT = "Password must contain at least one digit!"
    val ERR_UPPER = "Password must have at least one uppercase letter!"
    val ERR_SPECIAL = "Password must have at least one special character, such as: _%-=+#@"

    if (pwd.length <=8){
        Toast.makeText(context, ERR_LEN, Toast.LENGTH_SHORT).show()
        return false
    }else {
        return true
    }
}



@Composable
fun RegisterUser(
    context: Context,
    navController: NavController,
    viewModel: AuthenticationViewModel
) {
    RegistrationUI(onRegister = { email, password, username, profilePic, address, age ->  registerUser(email, password, username, profilePic, address, age, context, navController, viewModel) }, context = context, viewModel = viewModel, navController)
}

fun saveUserData(
    userId: String,
    email: String,
    username: String,
    profilePic: Uri?,
    address: String,
    age: String,
    context: Context,
    navController: NavController,
    viewModel: AuthenticationViewModel
) {
    initVar()
    storageRef = storageRef.child(System.currentTimeMillis().toString())
    profilePic?.let {
        storageRef.putFile(it)
            .addOnCompleteListener{StorageTask->
                if (StorageTask.isSuccessful){
                    storageRef.downloadUrl.addOnSuccessListener { uri->
                        val map = uri.toString()
                        val UserData =
                            UserData(
                                userID = userId,
                                email = email,
                                username = username,
                                profilePic = map,
                                address = address,
                                age = age,
                            )
                        val dbUserData: DocumentReference = firebaseFirestore.collection("Users").document(userId)
                        dbUserData.set(UserData)
                            .addOnCompleteListener{
                                if (it.isSuccessful){
                                    Toast.makeText(context,"Created user profile successfully", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent)
//                                    @Override
//                                    fun onBackPressed(){
//                                        moveTask
//                                    }
                                }else{
                                    Toast.makeText(context,it.exception?.message, Toast.LENGTH_SHORT).show()
                                    viewModel.isNotLoading()

                                }
                            }
                    }
                }else{
                    Toast.makeText(context,StorageTask.exception?.message, Toast.LENGTH_SHORT).show()

                }
            }

    }

}

fun registerUser(
    email: String, password: String, username: String, profilePic: Uri?, address: String, age: String,
    context: Context, navController: NavController, viewModel: AuthenticationViewModel
) {
    viewModel.isLoading()

    initVar()
    auth.createUserWithEmailAndPassword(email, password)

        .addOnCompleteListener{authenticatingtask->
            if(authenticatingtask.isSuccessful){
                saveUserData(userId = auth.currentUser?.uid ?:"", email,username, profilePic, address, age, context, navController, viewModel)/*Initial - giving this error: Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type FirebaseUser?*/
//                auth.currentUser?.let { saveUserData(it.uid, email,username, profilePic, address, age, context, navController)}
                Toast.makeText(context, "You have created your account successfully", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(context, authenticatingtask.exception?.message, Toast.LENGTH_SHORT).show()
                viewModel.isNotLoading()
            }
        }
}


fun initVar() {
    firebaseFirestore = FirebaseFirestore.getInstance()
    auth = FirebaseAuth.getInstance()
    storageRef = FirebaseStorage.getInstance().reference.child("Images")

}