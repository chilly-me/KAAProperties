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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.kaaproperties.PropertyViewModel
import com.example.kaaproperties.MainActivity
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.UserData
import com.example.kaaproperties.screens.components.customButton
import com.example.kaaproperties.screens.components.customPasswordTextField
import com.example.kaaproperties.screens.components.customTextField
import com.example.kaaproperties.ui.theme.AlegreyoSansFontFamily
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
    context: Context, viewModel: PropertyViewModel, navController: NavController,
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
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .height(150.dp)
                        .align(Alignment.Start)
                        .offset(x = (-20).dp)
                        .border(width = 0.dp, color = Color.Transparent),
                    colorFilter = ColorFilter.tint(Color.White)
                )

                Text(
                    text = "Sign Up",
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
                    text = "Create an Account to access our Services",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = AlegreyoSansFontFamily,
                        color = Color(0xB2FFFFFF)
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 12.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                val launcher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
                        profilePic = it
                    }
                if (profilePic == null) {
                    Image(
                        painter = painterResource(id = R.drawable.profilelogo),
                        contentDescription = "Profile Image",
                        Modifier
                            .clip(CircleShape)
                            .size(150.dp)
                            .clickable {
                                launcher.launch("image/*")
                            },
                        colorFilter = ColorFilter.tint(Color.White)

                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(profilePic),
                        contentDescription = "Profile Image",
                        Modifier
                            .clip(CircleShape)
                            .size(150.dp)
                            .clickable {
                                launcher.launch("image/*")
                            },
                        contentScale = ContentScale.Crop


                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                customTextField(
                    value = email,
                    placeHolder = "Email Address",
                    onValueChange = { email = it },
                    iconId = R.drawable.ic_email,
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
                customTextField(
                    value = username,
                    placeHolder = "Username",
                    onValueChange = { username = it },
                    iconId = R.drawable.ic_username,
                    viewModel = viewModel
                )
                customTextField(
                    value = address,
                    placeHolder = "Address",
                    iconId = R.drawable.ic_address,
                    onValueChange = { address = it },
                    viewModel = viewModel
                )
                customTextField(
                    value = age,
                    placeHolder = "Age",
                    iconId = R.drawable.ic_age,
                    onValueChange = { age = it },
                    viewModel = viewModel
                )
                Spacer(modifier = Modifier.height(20.dp))

                AnimatedVisibility(visible = viewModel.loading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(20.dp)
                    )

                }

                customButton(
                    onClick = {
                        validatePassword(password, context)
                        validateEmail(email, context)
                        if (profilePic == null){
                            Toast.makeText(context, "Upload your profile picture", Toast.LENGTH_SHORT).show()
                        }
                        val passwordvalidation = validatePassword(password, context)
                        val emailvalidation = validateEmail(email, context)
                        val isProfilePic = if (profilePic == null) false else true
                        if (passwordvalidation && emailvalidation && isProfilePic) {
                            onRegister(
                                email, password, username, profilePic, address, age
                            )
                        }
                    },
                    enabled = !viewModel.loading.value,
                    buttonText = "Sign Up",
                    color = 0xDA309FFF,
                    iconId = null,
                )

                Row(modifier = Modifier.padding(top = 12.dp, bottom = 52.dp))
                {
                    Text(
                        text = "Have an account?",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = AlegreyoSansFontFamily,
                            color = Color.White

                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Sign In.",
                        modifier = if (!viewModel.loading.value) {
                            Modifier
                                .clickable {
                                    navController.navigate(Screens.LoginScreen.route)
                                }
                        } else {
                            Modifier
                        },
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = AlegreyoSansFontFamily,
                            fontWeight = FontWeight(800),
                            color = Color.White,

                            ),


                        )
                }
            }
        }
    }


}

fun validateEmail(email: String, context: Context): Boolean {
    return if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        true
    } else {
        Toast.makeText(context, "Check your email Address", Toast.LENGTH_SHORT).show()
        false
    }

}

fun validatePassword(pwd: String, context: Context): Boolean {
    val ERR_LEN = "Password must have at least eight characters!"
    val ERR_WHITESPACE = "Password must not contain whitespace!"
    val ERR_DIGIT = "Password must contain at least one digit!"
    val ERR_UPPER = "Password must have at least one uppercase letter!"
    val ERR_SPECIAL = "Password must have at least one special character, such as: _%-=+#@"

    if (pwd.length <= 8) {
        Toast.makeText(context, ERR_LEN, Toast.LENGTH_SHORT).show()
        return false
    } else {
        return true
    }
}


@Composable
fun RegisterUser(
    context: Context,
    navController: NavController,
    viewModel: PropertyViewModel,
) {
    RegistrationUI(onRegister = { email, password, username, profilePic, address, age ->
        registerUser(
            email,
            password,
            username,
            profilePic,
            address,
            age,
            context,
            navController,
            viewModel
        )
    }, context = context, viewModel = viewModel, navController)
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
    viewModel: PropertyViewModel,
) {
    initVar()
    storageRef = storageRef.child(System.currentTimeMillis().toString())
    profilePic?.let {
        storageRef.putFile(it)
            .addOnCompleteListener { StorageTask ->
                if (StorageTask.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
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
                        val dbUserData: DocumentReference =
                            firebaseFirestore.collection("Users").document(userId)
                        dbUserData.set(UserData)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Created user profile successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate(Screens.Locations.route){
                                        navController.popBackStack()
                                    }

//                                    @Override
//                                    fun onBackPressed(){
//                                        moveTask
//                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        it.exception?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    viewModel.isNotLoading()

                                }
                            }
                    }
                } else {
                    Toast.makeText(context, StorageTask.exception?.message, Toast.LENGTH_SHORT)
                        .show()

                }
            }

    }

}

fun registerUser(
    email: String,
    password: String,
    username: String,
    profilePic: Uri?,
    address: String,
    age: String,
    context: Context,
    navController: NavController,
    viewModel: PropertyViewModel,
) {
    viewModel.isLoading()

    initVar()
    auth.createUserWithEmailAndPassword(email, password)

        .addOnCompleteListener { authenticatingtask ->
            if (authenticatingtask.isSuccessful) {
                saveUserData(
                    userId = auth.currentUser?.uid ?: "",
                    email,
                    username,
                    profilePic,
                    address,
                    age,
                    context,
                    navController,
                    viewModel
                )/*Initial - giving this error: Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type FirebaseUser?*/
//                auth.currentUser?.let { saveUserData(it.uid, email,username, profilePic, address, age, context, navController)}
                Toast.makeText(
                    context,
                    "You have created your account successfully",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                Toast.makeText(context, authenticatingtask.exception?.message, Toast.LENGTH_SHORT)
                    .show()
                viewModel.isNotLoading()
            }
        }
}


fun initVar() {
    firebaseFirestore = FirebaseFirestore.getInstance()
    auth = FirebaseAuth.getInstance()
    storageRef = FirebaseStorage.getInstance().reference.child("Images")

}