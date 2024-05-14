package com.example.kaaproperties.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kaaproperties.R
import com.example.kaaproperties.ui.theme.AlegreyoSansFontFamily

@Composable
fun customButton(
    onClick: () -> Unit,
    buttonText: String,
    color: Long = 0xFF676969,
    enabled: Boolean = true,
    iconId: Int?,
) {

    Button(
        onClick = { onClick() },
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(color)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(52.dp),
        enabled = enabled
    ) {
        if (iconId != null) {
            Box(modifier = Modifier) {
                Icon(
                    painter = painterResource(iconId),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))


        Text(
            text = buttonText,
            style = TextStyle(
                fontSize = 22.sp,
                fontFamily = AlegreyoSansFontFamily,
                fontWeight = FontWeight(500),
                color = Color.White
            )
        )


    }
}


