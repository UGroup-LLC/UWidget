package com.kikoproject.uwidget.navigation

sealed class ScreenNav(val route: String)
{
    object GoogleAuthNav : ScreenNav("google_auth_nav")
}