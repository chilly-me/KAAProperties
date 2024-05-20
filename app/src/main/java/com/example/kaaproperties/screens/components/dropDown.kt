package com.example.kaaproperties.screens.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kaaproperties.R
import com.example.kaaproperties.ui.theme.AlegreyoSansFontFamily

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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun customExposedDropdownMenuSample(
    options: List<String>,
    value: (String) -> Unit,
    iconId: Int? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 15.dp)
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            value = text ,
            textStyle = TextStyle(
                fontSize = 18.sp, fontFamily = AlegreyoSansFontFamily, color = Color(0xFFBEC2C2),
            ),
            onValueChange = {},
            readOnly = true,
            leadingIcon = {
                if (iconId != null){
                    Icon(painter = painterResource(id = iconId), contentDescription = null)
                }
            },
            singleLine = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xDA5F8BB1),
                focusedContainerColor = Color(0xDA5F8BB1),
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                focusedIndicatorColor = Color.White,
            ),

        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        value(option)
                        Log.d("month", option + " in dropDown")
                        text = option
                        expanded = false
                    },
                    leadingIcon = {
                        if (iconId != null){
                            Icon(painter = painterResource(id = iconId), contentDescription = null)
                        }
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}
