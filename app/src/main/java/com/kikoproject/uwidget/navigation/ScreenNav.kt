package com.kikoproject.uwidget.navigation

sealed class ScreenNav(val route: String)
{
    object MainNav : ScreenNav("main_nav")
    object GoogleAuthNav : ScreenNav("google_auth_nav")
    object RegistrationNav : ScreenNav("reg_nav")
    object ScheduleChooseNav : ScreenNav("schedule_nav")
    object AddScheduleNav : ScreenNav("add_schedule_nav")
    object Dashboard : ScreenNav("dashboard_nav")
}
sealed class PermissionNav(val route: String)
{
    object BackgroundActivity : PermissionNav("back_activity")
    object CreateWidget : PermissionNav("create_widget")
}
