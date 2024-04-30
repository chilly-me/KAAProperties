package com.example.kaaproperties.Authentication

import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
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
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.UserData
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
        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            profilePic = it
        }

        if (profilePic == null) {
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
            label = { Text("Address") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") }
        )
        Spacer(modifier = Modifier.height(11.dp))

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

        }
        ) {
            Text(text = "Register")
        }

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
    context: Context = LocalContext.current,
    navController: NavController
) {
    RegistrationUI(onRegister = { email, password, username, profilePic, address, age ->  registerUser(email, password, username, profilePic, address, age, context, navController) })
}

fun saveUserData(
    userId: String,
    email: String,
    username: String,
    profilePic: Uri?,
    address: String,
    age: String,
    context: Context,
    navController: NavController
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
                                    navController.navigate(Screens.Locations.route)
                                }else{
                                    Toast.makeText(context,it.exception?.message, Toast.LENGTH_SHORT).show()

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
    context: Context, navController: NavController
) {
    initVar()
    auth.createUserWithEmailAndPassword(email, password)

        .addOnCompleteListener{authenticatingtask->
            if(authenticatingtask.isSuccessful){
                saveUserData(userId = auth.currentUser?.uid ?:"", email,username, profilePic, address, age, context, navController)/*Initial - giving this error: Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type FirebaseUser?*/
//                auth.currentUser?.let { saveUserData(it.uid, email,username, profilePic, address, age, context, navController)}
                Toast.makeText(context, "You have created your account successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, authenticatingtask.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
}


fun initVar() {
    firebaseFirestore = FirebaseFirestore.getInstance()
    auth = FirebaseAuth.getInstance()
    storageRef = FirebaseStorage.getInstance().reference.child("Images")

}