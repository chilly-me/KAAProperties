package com.example.kaaproperties.screens.user

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
//import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference


private lateinit var auth: FirebaseAuth

@SuppressLint("StaticFieldLeak")
private lateinit var firebaseFirestore: FirebaseFirestore
private lateinit var storageRef: StorageReference

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScaffoldforUserProfile(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(text = "User Profile",fontSize = 20.sp )},
                navigationIcon = { IconButton(onClick = { navController.navigate(Screens.Locations.route) }) {
                    Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "Back")
                }}
            )
        },
    ) {
        Column(modifier = Modifier.padding(it)) {
            UserProfileScreen(navController = navController)

        }
    }
}

@Composable
fun UserProfileScreen(navController: NavController) {
    auth = FirebaseAuth.getInstance()
    var isloading by remember {
        mutableStateOf(true)
    }
    var userId = auth.currentUser?.uid
    val db:FirebaseFirestore = FirebaseFirestore.getInstance()
    var userData by remember(userId) {
        mutableStateOf(UserData())
    }

    if (userId != null) {
        val userRef = db.collection("Users").document(userId)
        userRef.get()
            .addOnSuccessListener {
                val user = it.toObject<UserData>()
                if (user != null) {
                    userData = user
                }
                isloading = false

            }
        val age = userData.age
        val name = userData.username
        val profilePic = userData.profilePic
        Log.d("UserInfo", "user age: $age, username: $name, profilePic: $profilePic")
    }
    Column(
        modifier = Modifier.padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isloading){
            Box(modifier = Modifier.fillMaxSize()){
                Column(modifier = Modifier.fillMaxSize()) {
                    AnimatedVisibility(visible = true) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(20.dp)
                                .size(150.dp),
                            strokeWidth = 10.dp,

                        )

                    }
                }
            }
        }else {
            AsyncImage(
                model = userData.profilePic,
                contentDescription = "Profile Picture",
                error = painterResource(id = R.drawable.profile),
                placeholder = painterResource(id = R.drawable.profile),
                modifier = Modifier
                    .clip(RectangleShape)
                    .height(210.dp)
                    .width(200.dp),
                contentScale = ContentScale.Fit,)
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE9E9E9))) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_username),
                    contentDescription = "UserName",
                    modifier = Modifier.weight(1f),

                    )
                Text(text = "Username: ${userData.username}", modifier = Modifier.weight(4f))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE9E9E9))) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_email),
                    contentDescription = "Email",
                    modifier = Modifier.weight(1f),

                    )
                Text(text = "Email: ${userData.email}", modifier = Modifier.weight(4f))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE9E9E9))) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_address),
                    contentDescription = "Address",
                    modifier = Modifier.weight(1f),

                    )
                Text(text = "Address: ${userData.address}", modifier = Modifier.weight(4f))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE9E9E9))) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_age),
                    contentDescription = "age",
                    modifier = Modifier.weight(1f),
                    )
                Text(text = "Age: ${userData.age}", modifier = Modifier.weight(4f))
            }
        }

    }


}


