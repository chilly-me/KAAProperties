package com.example.kaaproperties.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.kaaproperties.R

@Composable
fun customSearchBox(
    onValueChange: (String) -> Unit
) {

    var queryState by remember {
        mutableStateOf("")
    }
    TextField(
        value = queryState,
        onValueChange = {
            queryState = it
            onValueChange(it)
        },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_search_24), contentDescription = "Search") },
        singleLine = true

    )

}