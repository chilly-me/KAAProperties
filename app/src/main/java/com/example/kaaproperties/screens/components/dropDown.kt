package com.example.kaaproperties.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kaaproperties.R

@Composable
fun CustomDropDown(items: List<Pair<String, Int?>>, onClick: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
//            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                CustomDropDownMenuItem(item, onClick = {onClick(it)})
                HorizontalDivider()
            }

        }
    }
}
@Composable
fun CustomDropDownMenuItem(item: Pair<String, Int?>, onClick: (String) -> Unit) {
    DropdownMenuItem(
        text = { Text(item.first) },
        onClick = {onClick(item.first)},
        leadingIcon = {
            if(item.second != null){
                Image(
                    painter = painterResource(id = item.second!!),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        modifier = Modifier.width(200.dp)
        )
}

@Composable
fun customDropDownMenuItemForList() {
    DropdownMenuItem(
        text = { Text("Edit") },
        onClick = { /* Handle edit! */ },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        },
        modifier = Modifier.width(200.dp)
    )
}