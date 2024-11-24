package com.example.myapplication.presentation.screens.signup

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.presentation.components.DefaultTopBar
import com.example.myapplication.presentation.screens.signup.components.SignUp
import com.example.myapplication.presentation.screens.signup.components.SignupContent

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
