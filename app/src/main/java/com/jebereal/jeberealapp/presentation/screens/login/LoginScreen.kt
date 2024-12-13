package com.jebereal.jeberealapp.presentation.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jebereal.jeberealapp.presentation.screens.login.components.Login
import com.jebereal.jeberealapp.presentation.screens.login.components.LoginBottomBar
import com.jebereal.jeberealapp.presentation.screens.login.components.LoginContent


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

