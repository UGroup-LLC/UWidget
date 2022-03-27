package com.kikoproject.uwidget.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kikoproject.uwidget.auth.GoogleAuthScreen
import com.kikoproject.uwidget.auth.RegisterScreen
import com.kikoproject.uwidget.main.MainActivity
import com.kikoproject.uwidget.schedules.ChooseSchedule
import com.kikoproject.uwidget.ui.theme.Main

@Composable
fun NavigationSetup(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = ScreenNav.MainNav.route
    ) {
        composable(route = ScreenNav.GoogleAuthNav.route)
        {
            GoogleAuthScreen()
        }
        composable(route = ScreenNav.RegistrationNav.route)
        {
            RegisterScreen()
        }
        composable(route = ScreenNav.ScheduleChooseNav.route)
        {
            ChooseSchedule()
        }
        composable(route = ScreenNav.MainNav.route){
            MainActivity()
        }
    }
}
