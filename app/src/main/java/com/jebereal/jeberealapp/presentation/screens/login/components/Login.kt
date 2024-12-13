package com.jebereal.jeberealapp.presentation.screens.login.components



import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jebereal.jeberealapp.presentation.AppNavigation.AppScreen
import com.jebereal.jeberealapp.domain.model.Response
import com.jebereal.jeberealapp.presentation.components.ProgressBar
//import com.login.jetpackcompose.presentation.components.ProgressBar

import com.jebereal.jeberealapp.presentation.screens.login.LoginViewModel


@Composable
fun Login(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {

    when(val loginResponse = viewModel.loginResponse) {
        // MOSTRAR QUE SE ESTA REALIZANDO LA PETICION Y TODAVIA ESTA EN PROCESO
        Response.Loading -> {
            ProgressBar()
        }
        is Response.Success -> {
            LaunchedEffect(Unit) {
                navController.navigate(route = AppScreen.Main.route) {
                    popUpTo(AppScreen.Login.route) { inclusive = true }
                }
            }
        }
        is Response.Failure -> {
            Toast.makeText(LocalContext.current, loginResponse.exception?.message ?: "Error desconido", Toast.LENGTH_LONG).show()
        }

        else -> {}
    }

}