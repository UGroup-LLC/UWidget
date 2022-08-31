package com.kikoproject.uwidget.navigation

/**
 * Класс отвечающий за основные окна в навигации
 *
 * @author Kiko
 */
sealed class ScreenNav(val route: String)
{
    object MainNav : ScreenNav("main_nav")
    object GoogleAuthNav : ScreenNav("google_auth_nav")
    object RegistrationNav : ScreenNav("reg_nav")
    object ScheduleChooseNav : ScreenNav("schedule_nav")
    object AddScheduleNav : ScreenNav("add_schedule_nav")
    object Dashboard : ScreenNav("dashboard_nav")
    object AllSchedulesNav : ScreenNav("all_schedule_nav")
    object EditScheduleNav : ScreenNav("edit_schedule_nav")
    object ShowJoinCodeNav : ScreenNav("show_join_code_nav")
    object EditScheduleMenuNav : ScreenNav("edit_scheduleMenu_nav")
    object JoinToScheduleNav : ScreenNav("join_to_schedule_nav")
    object EditMembersNav : ScreenNav("edit_members_nav")
    object OptionsNav : ScreenNav("options_nav")

}
sealed class PermissionNav(val route: String)
{
    object CreateWidget : PermissionNav("create_widget")
}
