package com.example.myapplication.presentation.screens.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            .fillMaxSize()
            .background(Color(0xFFFFFFFF)) // Fondo blanco
    ) {

        // Parte superior con fondo rosado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(Color(0xFFD67D79)) // Fondo rosado
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.height(120.dp),
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

        // Card del formulario con bordes redondeados
        Card(
            modifier = Modifier
                .padding(top = 200.dp, start = 1.dp, end = 1.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = Color.White // Fondo blanco del formulario
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                    text = "Gmail:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD67D79),
                )


                // Campo de correo electrónico centrado
                Box(
                    modifier = Modifier
                        .fillMaxWidth() // Asegura que el campo ocupe todo el ancho disponible
                        .padding(horizontal = 20.dp) // Añade espaciado a los lados
                ) {
                    DefaultTextField(
                        modifier = Modifier
                            .align(Alignment.Center) // Centra el campo dentro del Box
                            .padding(top = 5.dp),
                        value = state.email,
                        onValueChange = { viewModel.onEmailInput(it) },
                        label = "Correo electrónico",
                        icon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email,
                        errorMsg = viewModel.emailErrMsg,
                        validateField = { viewModel.validateEmail() }
                    )
                }

                Text(
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                    text = "Contraseña:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD67D79), // Color del texto igual al fondo

                )

                // Campo de contraseña
                Box(
                    modifier = Modifier
                        .fillMaxWidth() // Asegura que el campo ocupe todo el ancho disponible
                        .padding(horizontal = 20.dp) // Añade espaciado a los lados
                ) {
                    DefaultTextField(
                        modifier = Modifier
                            .align(Alignment.Center) // Centra el campo dentro del Box
                            .padding(top = 5.dp),
                        value = state.password,
                        onValueChange = { viewModel.onPasswordInput(it) },
                        label = "Contraseña",
                        icon = Icons.Default.Lock,
                        hideText = true, // Oculta el texto ingresado
                        errorMsg = viewModel.passwordErrMsg,
                        validateField = { viewModel.validatePassword() }
                    )
                }
                Text(
                    text = "                                ¿Olvidaste tu contraseña?",
                    fontSize = 15.sp,
                    color = Color.Black
                )

                // Botón de iniciar sesión
                DefaultButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp),
                    text = "INICIAR SESIÓN",
                    onClick = {
                        viewModel.login()
                    },
                    enabled = viewModel.isEnabledLoginButton,
                    // Definición de los colores para el botón
                    color = Color(0xFFE57373), // Color de fondo del botón (rojo)

                )

            }
        }
    }
}
