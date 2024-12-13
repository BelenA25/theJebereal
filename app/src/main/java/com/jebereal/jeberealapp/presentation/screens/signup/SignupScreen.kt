package com.jebereal.jeberealapp.presentation.screens.signup

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.jebereal.jeberealapp.presentation.components.DefaultTopBar
import com.jebereal.jeberealapp.presentation.screens.signup.components.SignUp
import com.jebereal.jeberealapp.presentation.screens.signup.components.SignupContent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignupScreen(navController: NavHostController) {
    
    Scaffold(
        topBar = {
             DefaultTopBar(
                 title = "Nuevo usuario",
                 upAvailable = true,
                 navController = navController
             )
        },
        content = {
            SignupContent(navController)
        },
        bottomBar = {}
    )
    SignUp(navController = navController)
    
}
