package com.kikoproject.uwidget.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.auth.api.signin.GoogleSignIn

@Composable
fun DashboardActivity(){
    val context = LocalContext.current
    val account = GoogleSignIn.getLastSignedInAccount(context)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(10.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column() {
            Row(){
                Button(onClick = { /*TODO*/ }) {

                }
                Text(text = "Главная")

            }

            Card(
                shape = RoundedCornerShape(15.dp),
                contentColor = MaterialTheme.colors.primary,
                border = BorderStroke(1.dp, MaterialTheme.colors.secondary)
            ) {
            }
        }
    }
}