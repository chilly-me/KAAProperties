package com.example.kaaproperties.screens.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.UserData
import com.example.kaaproperties.screens.components.customButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
//import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference


private lateinit var auth: FirebaseAuth

@SuppressLint("StaticFieldLeak")
private lateinit var firebaseFirestore: FirebaseFirestore
private lateinit var storageRef: StorageReference


@Composable
fun UserProfileScreen(modifier: Modifier = Modifier, navController: NavController, context: Context) {
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
    }else{
        navController.navigate(Screens.Locations.route)
        Toast.makeText(context, "You are not logged in", Toast.LENGTH_SHORT).show()
    }
    if (isloading){

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedVisibility(visible = true) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(20.dp)
                        .size(150.dp),
                    strokeWidth = 10.dp,

                    )

            }
        }
    }else{

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .background(Color(0xFFF2F1F6)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout(
                modifier = modifier
                    .height(250.dp)
                    .background(Color(0xDA035092))
            ) {
                val (topImg, profile, title, back, pen, user) = createRefs()
                Image(
                    painter = painterResource(id = R.drawable.arc_3),
                    contentDescription = null,
                    modifier = modifier
                        .fillMaxWidth()
                        .constrainAs(topImg) {
                            bottom.linkTo(parent.bottom)
                        },
                    contentScale = ContentScale.FillBounds
                )
                AsyncImage(
                    model = userData.profilePic,
                    contentDescription = "Profile Picture",
                    error = painterResource(id = R.drawable.profile),
                    placeholder = painterResource(id = R.drawable.profile),
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(100.dp)
                        .constrainAs(profile) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(topImg.bottom)
                        },
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = "Profile",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 30.sp,
                    ),
                    modifier = modifier
                        .constrainAs(title) {
                            top.linkTo(parent.top, margin = 24.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(topImg.bottom, margin = 15.dp)
                        }


                )
                IconButton(
                    onClick = { navController.navigate(Screens.Locations.route) },
                    modifier = modifier
                        .constrainAs(back) {
                            top.linkTo(parent.top, margin = 24.dp)
                            start.linkTo(parent.start, margin = 24.dp)

                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        tint = Color.White
                    )

                }


            }
            Text(
                text = userData.username,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    color = Color(0xFF32357A)
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
            )
            customRow(imgId = R.drawable.ic_email, contentDescription = "Email", text = userData.email)
            customRow(imgId = R.drawable.ic_age, contentDescription = "Age", text = userData.age)
            customRow(imgId = R.drawable.ic_address, contentDescription = "Email", text = userData.address)
            Spacer(modifier = Modifier.weight(1f))
            customButton(
                onClick = {
                    auth.signOut()

                    Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screens.WelcomeScreen.route)
                },
                buttonText = "Sign Out",
                iconId = null
            )


        }
    }


}


@Composable
fun customRow(modifier: Modifier = Modifier, imgId: Int, contentDescription: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = modifier
                .height(40.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = imgId),
                contentDescription = contentDescription,
                modifier = modifier
                    .padding(end = 5.dp)
            )

        }
        Column(
            modifier = modifier
                .padding(start = 16.dp,)
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

        }
    }
}
