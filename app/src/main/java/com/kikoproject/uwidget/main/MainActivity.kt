package com.kikoproject.uwidget.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kikoproject.uwidget.navigation.NavigationSetup
import com.kikoproject.uwidget.networking.CheckUserInDB
import com.kikoproject.uwidget.ui.theme.UWidgetTheme

@SuppressLint("StaticFieldLeak")
lateinit var navController: NavHostController
@SuppressLint("StaticFieldLeak")
val db = Firebase.firestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UWidgetTheme {
                analyticsEnable() // Включение аналитики
                Wait()
                val context = LocalContext.current
                navController = rememberNavController()
                NavigationSetup(navController = navController)
                val state = remember { mutableStateOf(true) }
                CheckUserInDB(
                    context = context,
                    state = state,
                    textError = "Автономный режим",
                )
            }
        }
    }
}

@Composable
fun Wait() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.TopCenter
    ) {
    }

}

@Preview(showBackground = true)
@Composable
fun AuthPreview() {
    Wait()
}

fun analyticsEnable() {
    val analytics = Firebase.analytics
}