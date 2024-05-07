package com.example.kaaproperties

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel

class AuthenticationViewModel: ViewModel() {
    var loading = mutableStateOf(false)

    fun isLoading(){
        loading.value = true
    }
    fun isNotLoading(){
        loading.value = false
    }
}


