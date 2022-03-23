package com.kikoproject.uwidget.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kikoproject.uwidget.auth.GoogleAuthScreen

@Composable
fun NavigationSetup(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = ScreenNav.GoogleAuthNav.route
    ) {
        composable(route = ScreenNav.GoogleAuthNav.route)
        {
            GoogleAuthScreen()
        }
    }
}
