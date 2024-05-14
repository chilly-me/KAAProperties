@file:Suppress("DEPRECATION")

package com.example.kaaproperties.Authentication.email

import android.content.Context
import android.content.Intent
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.kaaproperties.screens.components.customButton
import com.example.kaaproperties.ui.theme.AlegreyaFontFamily
import com.example.kaaproperties.ui.theme.AlegreyoSansFontFamily
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Loginscreen2(viewModel: PropertyViewModel) {

}

@Composable
fun Loginscreen3(viewModel: PropertyViewModel) {

}






















