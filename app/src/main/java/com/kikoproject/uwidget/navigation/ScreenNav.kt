package com.kikoproject.uwidget.navigation

sealed class ScreenNav(val route: String)
{
    object MainNav : ScreenNav("main_nav")
    object GoogleAuthNav : ScreenNav("google_auth_nav")
    object RegistrationNav : ScreenNav("reg_nav")
    object ScheduleChooseNav : ScreenNav("schedule_nav")
}