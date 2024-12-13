package com.jebereal.jeberealapp.domain.use_cases.auth

data class AuthUseCases (
    val getCurrentUser: GetCurrentUser,
    val login: Login,
    val logout: Logout,
    val signup: Signup
)