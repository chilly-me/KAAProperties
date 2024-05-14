package com.example.kaaproperties.screens.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.kaaproperties.R
import com.example.kaaproperties.ui.theme.AlegreyoSansFontFamily

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun customDailog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    headerText: String,
    image: @Composable () -> Unit,
    content: @Composable () -> Unit,
    saveButton: @Composable () -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(Color(0xFFF2F1F6))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout(
                modifier = modifier
                    .height(150.dp)
                    .background(Color(0xDA035092))
            ) {
                val (topImg, logo, title, back, pen, user) = createRefs()
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
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(100.dp)
                        .constrainAs(logo) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(topImg.bottom, margin = 30.dp)
                        },
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(Color.White)

                )


                IconButton(
                    onClick = { onDismiss() },
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
            Text(text = headerText, fontSize = 20.sp, fontFamily = AlegreyoSansFontFamily)
            Spacer(modifier = Modifier.height(20.dp))
            image()


            Spacer(modifier = Modifier.height(20.dp))
            var email by mutableStateOf("")
            content()


            Spacer(modifier = Modifier.height(50.dp))
            saveButton()



        }
    }
}