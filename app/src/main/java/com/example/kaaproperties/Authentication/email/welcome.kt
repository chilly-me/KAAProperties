@file:Suppress("DEPRECATION")

package com.example.kaaproperties.Authentication.email

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kaaproperties.PropertyViewModel
import com.example.kaaproperties.Navigation.Screens
import com.example.kaaproperties.R
import com.example.kaaproperties.logic.UserData
import com.example.kaaproperties.screens.components.customButton
import com.example.kaaproperties.screens.location.signOutUser
import com.example.kaaproperties.ui.theme.AlegreyaFontFamily
import com.example.kaaproperties.ui.theme.AlegreyoSansFontFamily
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private lateinit var auth: FirebaseAuth

@SuppressLint("StaticFieldLeak")
private lateinit var firebaseFirestore: FirebaseFirestore
private lateinit var storageRef: StorageReference
@Composable
fun welcomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var user by remember {
        mutableStateOf(Firebase.auth.currentUser)
    }
    val context = LocalContext.current
    var launcher =
        GoogleAuth(
            onAuthComplete = { result -> user = result.user },
            onAuthError = { user = null },
            navController = navController,
            context = context
        )
    var token = stringResource(id = R.string.default_web_client_id)
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.apartment13),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .width(320.dp)
                    .height(240.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color.White)


            )
            Text(
                text = "WELCOME",
                fontSize = 32.sp,
                fontFamily = AlegreyaFontFamily,
                fontWeight = FontWeight(700),
                color = Color.White

            )
            Text(
                text = "Manage your Property, \n The Right Way",
                textAlign = TextAlign.Center,
                fontFamily = AlegreyoSansFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight(500),
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(0.75f))

            customButton(
                onClick = {navController.navigate(Screens.LoginScreen.route)},
                buttonText = "Sign In With Email",
                iconId = R.drawable.ic_email
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
                    modifier = Modifier.clickable { navController.navigate(Screens.SignUp.route) },
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = AlegreyoSansFontFamily,
                        fontWeight = FontWeight(800),
                        color = Color.White
                    )
                )
            }
            Text(
                text = "or",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = AlegreyoSansFontFamily,
                    fontWeight = FontWeight(800),
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.weight(0.25f))
            customButton(
                onClick = {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(token)
                        .requestEmail()
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    signOutUser(googleSignInClient)
                    launcher.launch(googleSignInClient.signInIntent)
                },
                buttonText = "Sign In with google",
                iconId = R.drawable.google
            )
            Spacer(modifier = Modifier.weight(0.5f))


        }

    }
}

@Composable
fun GoogleAuth(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit,
    context: Context,
    navController: NavController
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            Log.d("GoogleAuth", "account $account")
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            scope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                val user = Firebase.auth.currentUser
                val email = user?.email
                val userId = user?.uid
                val photoUrl = user?.photoUrl.toString()
                val name = user?.displayName
                Log.d("saving", "is userId null in ? $userId")
                Log.d("saving", "profile picture: $photoUrl")

                saveUserDataForGoogleAuthentications(
                    userId = userId,
                    email = email,
                    ProfileUrl = photoUrl,
                    username = name,
                    age = null,
                    address = null,
                    context = context,
                    navController = navController,
                    viewModel = null
                )
            Toast.makeText(context, "Successful Login", Toast.LENGTH_SHORT).show()
                navController.navigate(Screens.Locations.route){
                    navController.popBackStack()
                }
                onAuthComplete(authResult)
            }
        } catch (e: ApiException) {
            Log.d("GoogleAuth", e.toString())
            onAuthError(e)
        }
    }
}




fun saveUserDataForGoogleAuthentications(
    userId: String?,
    email: String?,
    username: String?,
    ProfileUrl: String?,
    address: String?,
    age: String?,
    context: Context,
    navController: NavController,
    viewModel: PropertyViewModel?,
) {
    Log.d("saving", "Entered composable")

    firebaseFirestore = FirebaseFirestore.getInstance()
    Log.d("saving", "is userId null in composable? $userId")

    val UserData =
        UserData(
            userID = userId,
            email = email,
            username = username,
            profilePic = ProfileUrl,
            address = address,
            age = age,
        )
    Log.d("saving", "is userId null in composable? $userId")

    if (userId != null) {
        val dbUserData: DocumentReference = firebaseFirestore.collection("Users").document(userId)
        dbUserData.get()
            .addOnSuccessListener {
                if (it.exists()){

                }else{
                    dbUserData.set(UserData)
                        .addOnCompleteListener {
                            Log.d("saving", "saving to firestore")

                            if (it.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Created user profile successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate(Screens.Locations.route) {
                                    navController.popBackStack()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    it.exception?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                viewModel?.isNotLoading()

                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d("saving","$exception")
                        }
                }
            }


}
}
















