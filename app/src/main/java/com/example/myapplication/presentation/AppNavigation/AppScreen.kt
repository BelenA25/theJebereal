package com.example.myapplication.presentation.AppNavigation


sealed class AppScreen(val route: String) {
    object Welcome: AppScreen("welcome")
    object Login: AppScreen("login")
    object Signup: AppScreen("signup")
    object Profile: AppScreen("profile")
    object Main : AppScreen("main")
    object ProfileUpdate: AppScreen("profile/update/{user}") {
        fun passUser(user: String) = "profile/update/$user"
    }
}
