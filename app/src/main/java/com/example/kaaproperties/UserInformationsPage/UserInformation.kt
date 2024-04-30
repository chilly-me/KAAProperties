package com.example.kaaproperties.UserInformationsPage

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.kaaproperties.Authentication.initVar
import com.example.kaaproperties.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
//import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await


private lateinit var auth: FirebaseAuth

@SuppressLint("StaticFieldLeak")
private lateinit var firebaseFirestore: FirebaseFirestore
private lateinit var storageRef: StorageReference

@Composable
fun UserProfileScreen(navController: NavController) {
    auth = FirebaseAuth.getInstance()
    firebaseFirestore = FirebaseFirestore.getInstance()

    var userId = auth.currentUser?.uid
    var userData by remember(userId) {
        mutableStateOf(UserData())
    }

    LaunchedEffect(userId) {
        if (userId != null) {
            val userRef = firebaseFirestore.collection("Users").document(userId)
            val userSnapshot = userRef.get().await()

            if (userSnapshot.exists()) {
                val user = userSnapshot.toObject<UserData>()
                user?.let {
                    userData = it
                }
            }
        }

    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = rememberAsyncImagePainter({userData.profilePic}), contentDescription = "Profile Picture" )
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "User ID: ${userData.userID}")
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Email: ${userData.email}")
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Username: ${userData.username}")
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Address: ${userData.address}")
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Age: ${userData.age}")
        Spacer(modifier = Modifier.height(10.dp))
    }
}

