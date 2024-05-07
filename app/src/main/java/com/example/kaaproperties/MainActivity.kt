package com.example.kaaproperties

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kaaproperties.Navigation.Navigation
import com.example.kaaproperties.flows.DBHelperImpl
import com.example.kaaproperties.flows.DBhelper
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.room.database.PropertyDatabase
import com.example.kaaproperties.ui.theme.KAAPropertiesTheme

class MainActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val dao = PropertyDatabase.getInstance(applicationContext).propertyDao
            val viewModel by viewModels<PropertyViewModel>(
                factoryProducer = {
                    object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            @Suppress("UNCHECKED_CAST")
                            return PropertyViewModel(dao) as T
                        }
                    }
                }
            )
            val state by viewModel.state.collectAsState()
            val dbHelper = DBHelperImpl(dao, state)
            val x = viewModel::onEvent
            x((Events.showLocations), dbHelper)
            KAAPropertiesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    AddingLocation(states = state, onEvent = viewModel::onEvent)

                    Navigation(states = state, onEvents = viewModel::onEvent , context = LocalContext.current)


                }
            }
        }
    }
}



