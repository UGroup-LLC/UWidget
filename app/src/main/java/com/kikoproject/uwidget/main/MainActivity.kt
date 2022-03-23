package com.kikoproject.uwidget.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.rememberNavController
import com.kikoproject.uwidget.navigation.NavigationSetup
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.ui.theme.UWidgetTheme

@SuppressLint("StaticFieldLeak")
lateinit var navController: NavHostController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UWidgetTheme() {
                navController = rememberNavController()
                NavigationSetup(navController = navController)
            }
        }
    }

    @Composable
    fun Greeting() {
        Text(text = "Wait")
    }

    @Preview(showBackground = true)
    @Composable
    fun AuthPreview() {
        Greeting()
    }
}