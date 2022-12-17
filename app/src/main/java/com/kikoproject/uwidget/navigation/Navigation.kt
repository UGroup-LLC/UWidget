package com.kikoproject.uwidget.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kikoproject.uwidget.auth.GoogleAuthScreen
import com.kikoproject.uwidget.auth.RegisterScreen
import com.kikoproject.uwidget.main.DashboardActivity
import com.kikoproject.uwidget.main.MainActivity
import com.kikoproject.uwidget.main.OptionsActivity
import com.kikoproject.uwidget.permissions.CreateWidgetActivity
import com.kikoproject.uwidget.schedules.*

/**
 * Установка NavHost маршрутов для контроллера навигации
 *
 * @author Kiko
 */
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
        composable(route = ScreenNav.ShowJoinCodeNav.route){
            ShowJoinCode()
        }
        composable(route = ScreenNav.EditScheduleMenuNav.route){
            EditScheduleMenu()
        }
        composable(route = ScreenNav.JoinToScheduleNav.route){
            JoinSchedule()
        }
        composable(route = ScreenNav.EditMembersNav.route){
            EditScheduleMembers()
        }

        //permission
        composable(route = PermissionNav.CreateWidget.route){
            CreateWidgetActivity()
        }

        //options
        composable(route = ScreenNav.OptionsNav.route){
            OptionsActivity()
        }
    }
}
