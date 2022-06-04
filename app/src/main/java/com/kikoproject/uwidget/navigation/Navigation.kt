package com.kikoproject.uwidget.navigation

import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kikoproject.uwidget.auth.GoogleAuthScreen
import com.kikoproject.uwidget.auth.RegisterScreen
import com.kikoproject.uwidget.main.DashboardActivity
import com.kikoproject.uwidget.main.MainActivity
import com.kikoproject.uwidget.permissions.BackgroundPermissionActivity
import com.kikoproject.uwidget.permissions.CreateWidgetActivity
import com.kikoproject.uwidget.schedules.AddSchedule
import com.kikoproject.uwidget.schedules.AllSchedulesActivity
import com.kikoproject.uwidget.schedules.ChooseSchedule
import com.kikoproject.uwidget.schedules.EditSchedule
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
        composable(route = ScreenNav.AddScheduleNav.route){
            AddSchedule()
        }
        composable(route = ScreenNav.Dashboard.route){
            DashboardActivity()
        }
        composable(route = ScreenNav.AllSchedulesNav.route){
            AllSchedulesActivity()
        }
        composable(route = ScreenNav.EditScheduleNav.route){
            EditSchedule()
        }


        //permission
        composable(route = PermissionNav.BackgroundActivity.route){
            BackgroundPermissionActivity()
        }
        composable(route = PermissionNav.CreateWidget.route){
            CreateWidgetActivity()
        }
    }
}
