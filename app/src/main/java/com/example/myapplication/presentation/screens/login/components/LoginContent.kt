package com.example.myapplication.presentation.screens.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.myapplication.R

import com.example.myapplication.presentation.components.DefaultButton
import com.example.myapplication.presentation.components.DefaultTextField
import com.example.myapplication.presentation.screens.login.LoginViewModel




@Composable
fun LoginContent(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {

    val state = viewModel.state

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFAD4D3)), // Fondo de la parte superior
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(Color(0xFFE57373)), // Parte superior naranja
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.height(130.dp),
                    painter = painterResource(id = R.drawable.users),
                    contentDescription = "Logo"
                )
                Text(
                    text = "Inicia sesión",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Card with rounded corners for the login form
        Card(
            modifier = Modifier
                .padding(start = 40.dp, end = 40.dp, top = 200.dp)
                .background(Color.White, shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
        ) {

            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 40.dp, bottom = 10.dp),
                    text = "LOGIN",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Por favor inicia sesión para continuar",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                DefaultTextField(
                    modifier = Modifier.padding(top = 25.dp),
                    value = state.email,
                    onValueChange = { viewModel.onEmailInput(it) },
                    label = "Correo electrónico",
                    icon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email,
                    errorMsg = viewModel.emailErrMsg,
                    validateField = { viewModel.validateEmail() }
                )

                DefaultTextField(
                    modifier = Modifier.padding(top = 0.dp),
                    value = state.password,
                    onValueChange = { viewModel.onPasswordInput(it) },
                    label = "Contraseña",
                    icon = Icons.Default.Lock,
                    hideText = true,
                    errorMsg = viewModel.passwordErrMsg,
                    validateField = { viewModel.validatePassword() }
                )

                DefaultButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp),
                    text = "INICIAR SESIÓN",
                    onClick = {
                        viewModel.login()
                    },
                    enabled = viewModel.isEnabledLoginButton,
                    color = Color(0xFFE57373) // Botón rojo similar al color de la imagen
                )
            }
        }
    }
}
