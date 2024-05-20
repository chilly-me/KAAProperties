package com.example.kaaproperties.screens.user

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.logic.UserData
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.screens.components.customButton
import com.example.kaaproperties.ui.theme.AlegreyoSansFontFamily
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
//import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference


private lateinit var auth: FirebaseAuth

@SuppressLint("StaticFieldLeak")
private lateinit var firebaseFirestore: FirebaseFirestore
private lateinit var storageRef: StorageReference


@SuppressLint("UnrememberedMutableState")
@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    context: Context,
    onEvents: (Events) -> Unit,
    states: states,
) {
    auth = FirebaseAuth.getInstance()
    var isloading by remember {
        mutableStateOf(true)
    }
    var userId = auth.currentUser?.uid
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var userData by remember(userId) {
        mutableStateOf(UserData())
    }
    var success by remember {
        mutableStateOf(true)
    }
    var email by remember {
        mutableStateOf("")
    }
    var profilePic by remember {
        mutableStateOf("")
    }
    var name by remember {
        mutableStateOf("")
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
                success = true

            }
            .addOnFailureListener {
                val currentUser = auth.currentUser
                success = false
                name = currentUser?.displayName.toString()
                profilePic= currentUser?.photoUrl.toString()
                email = currentUser?.email.toString()


            }


    } else {
        navController.navigate(Screens.WelcomeScreen.route)
        Toast.makeText(context, "You are not logged in", Toast.LENGTH_SHORT).show()
    }
    if (isloading) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = true) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(20.dp)
                        .size(150.dp),
                    strokeWidth = 10.dp,

                    )

            }
        }
    } else {
        if (success){
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
                userData.username?.let {
                    Text(
                        text = it,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            color = Color(0xFF32357A)
                        ),
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )
                }

                userData.email?.let {
                    customRow(
                        imgId = R.drawable.ic_email,
                        contentDescription = "Email",
                        text = it
                    )
                }
                userData.age?.let { customRow(imgId = R.drawable.ic_age, contentDescription = "Age", text = it) }
                userData.address?.let {
                    customRow(
                        imgId = R.drawable.ic_address,
                        contentDescription = "Email",
                        text = it
                    )
                }
                Spacer(modifier = Modifier.weight(0.4f))
                customButton(
                    onClick = {
                        auth.signOut()
                        Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screens.WelcomeScreen.route)
                    },
                    color = 0xFFFA2323,
                    buttonText = "Sign Out",
                    iconId = null,
                    width = 0.8f
                )


            }
        }else{
            GoogleAuthenticated(
                profileImage = profilePic,
                navController = navController,
                userName = name,
                email = email,
                context = context
            )
        }


    }


}




@Composable
fun customRow(
    modifier: Modifier = Modifier,
    imgId: Int?,
    contentDescription: String,
    text: String,
    fontWeight: FontWeight = FontWeight.Normal,
    clickIcon: (() -> Unit)? = null,
) {
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
            imgId?.let { painterResource(id = it) }?.let {
                Image(
                    painter = it,
                    contentDescription = contentDescription,
                    modifier = modifier
                        .padding(end = 5.dp)
                        .clickable {
                            if (clickIcon != null) {
                                clickIcon()
                            }
                        }
                )
            }

        }
        Column(
            modifier = modifier
                .padding(start = 16.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = Color.Black,
                fontSize = 18.sp,
                fontFamily = AlegreyoSansFontFamily,
                fontWeight = fontWeight,
                modifier = Modifier
                    .clickable {
                        if (clickIcon != null) {
                            clickIcon()
                        }
                    }
            )

        }
    }
}

@Composable
fun GoogleAuthenticated(
    modifier: Modifier = Modifier,
    profileImage: String,
    navController: NavController,
    userName: String,
    email: String,
    context: Context
) {
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
                model = profileImage,
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
            text = userName,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                color = Color(0xFF32357A)
            ),
            modifier = Modifier
                .padding(top = 16.dp)
        )

        customRow(
            imgId = R.drawable.ic_email,
            contentDescription = "Email",
            text = email
        )
        Spacer(modifier = Modifier.weight(0.4f))
        customButton(
            onClick = {
                auth.signOut()
                Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()
                navController.navigate(Screens.WelcomeScreen.route)
            },
            color = 0xFFFA2323,
            buttonText = "Sign Out",
            iconId = null,
            width = 0.8f
        )


    }
}
