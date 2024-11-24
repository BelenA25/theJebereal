package com.example.myapplication.presentation.screens.profile


import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.login.jetpackcompose.presentation.screens.profile.components.ProfileContent


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(navController: NavHostController, viewModel: ProfileViewModel = hiltViewModel()) {

    Scaffold(
        topBar = {},
        content = {
              ProfileContent(navController)
        },
        bottomBar = {}
    )

}