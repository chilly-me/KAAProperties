package com.example.kaaproperties

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kaaproperties.Navigation.Navigation
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.room.database.PropertyDatabase
import com.example.kaaproperties.ui.theme.KAAPropertiesTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MainActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            var firebaseFirestore = FirebaseFirestore.getInstance()
            var auth = FirebaseAuth.getInstance()
            var storageRef = FirebaseStorage.getInstance().reference.child("ImagesForProperties")
            val context = LocalContext.current
            val dao = PropertyDatabase.getInstance(applicationContext).propertyDao
            val viewModel by viewModels<PropertyViewModel>(
                factoryProducer = {
                    object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            @Suppress("UNCHECKED_CAST")
                            return PropertyViewModel(
                                dao = dao,
                                firebaseFirestore = firebaseFirestore,
                                auth = auth,
                                storageRef = storageRef,
                                context = context
                            ) as T
                        }
                    }
                }
            )
            val state by viewModel.state.collectAsState()
            val x = viewModel::onEvent
            x((Events.showLocations))
            x((Events.showTenants))
            x((Events.showProperies))
            x((Events.showPayments))
            KAAPropertiesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    AddingLocation(states = state, onEvent = viewModel::onEvent)

                    Navigation(states = state, onEvents = viewModel::onEvent , context = LocalContext.current, viewModel = viewModel)


                }
            }
        }
    }
}

