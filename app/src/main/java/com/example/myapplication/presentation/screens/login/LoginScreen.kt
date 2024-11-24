package com.example.myapplication.presentation.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.presentation.screens.login.components.Login
import com.example.myapplication.presentation.screens.login.components.LoginBottomBar
import com.example.myapplication.presentation.screens.login.components.LoginContent


@Composable
fun LoginScreen(navController: NavHostController) {

    Scaffold(
        topBar = {},
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(bottom = 50.dp) // Ajusta el margen inferior
            ) {
                LoginBottomBar(navController)
            }
        },
        content = { paddingValues -> // Usa PaddingValues proporcionados por Scaffold
            Box(
                modifier = Modifier
                    .padding(paddingValues) // Aplica los PaddingValues aquí
                    .fillMaxSize()
            ) {
                LoginContent(navController)
            }
        }
    )
    // MANEJAR EL ESTADO DE LA PETICION DE LOGIN
    Login(navController = navController)

}

