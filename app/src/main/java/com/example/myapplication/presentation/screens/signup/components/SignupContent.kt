package com.example.myapplication.presentation.screens.signup.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
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
import com.example.myapplication.presentation.screens.signup.SignupViewModel


@Composable
fun SignupContent(navController: NavHostController,  viewModel: SignupViewModel = hiltViewModel()) {

    val state = viewModel.state

    // Parte superior con fondo rosado
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .background(Color(0xFFD67D79))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Registrarte",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }



    Box {
        Card(
            modifier = Modifier
                .padding(top = 160.dp, start = 1.dp, end = 1.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = Color.White // Fondo blanco del formulario
            )
        ){

            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {

                Spacer(modifier = Modifier.height(10.dp))


                Box(
                    modifier = Modifier
                        .fillMaxSize(), // Ocupa toda la pantalla
                    contentAlignment = Alignment.Center // Centra todo el contenido
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f) // Ajusta el ancho de la columna
                            .padding(16.dp), // Espaciado interno de los bordes
                        horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos horizontalmente
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(top = 5.dp, bottom = 10.dp)
                                .align(Alignment.Start), // Alinea el texto a la izquierda
                            text = "Nombre Completo:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD67D79),
                        )

                        DefaultTextField(
                            modifier = Modifier.padding(top = 0.dp),
                            value = state.username,
                            onValueChange = { viewModel.onUsernameInput(it) },
                            label = "Nombre de usuario",
                            icon = Icons.Default.Person,
                            errorMsg = viewModel.usernameErrMsg,
                            validateField = { viewModel.validateUsername() }
                        )
                        Text(
                            modifier = Modifier
                                .padding(top = 1.dp, bottom = 5.dp)
                                .align(Alignment.Start), // Alinea el texto a la izquierda
                            text = "Gmail:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD67D79),
                        )
                        DefaultTextField(
                            modifier = Modifier.padding(top = 0.dp),
                            value = state.email,
                            onValueChange = { viewModel.onEmailInput(it) },
                            label = "Correo electronico",
                            icon = Icons.Default.Email,
                            keyboardType = KeyboardType.Email,
                            errorMsg = viewModel.emailErrMsg,
                            validateField = { viewModel.validateEmail() }
                        )
                        Text(
                            modifier = Modifier
                                .padding(top = 1.dp, bottom = 10.dp)
                                .align(Alignment.Start), // Alinea el texto a la izquierda
                            text = "Contraseña:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD67D79),
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
                        Text(
                            modifier = Modifier
                                .padding(top = 1.dp, bottom = 10.dp)
                                .align(Alignment.Start), // Alinea el texto a la izquierda
                            text = "Confirmar la contraseña:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,                            color = Color(0xFFD67D79),
                        )
                        DefaultTextField(
                            modifier = Modifier.padding(top = 0.dp),
                            value = state.confirmPassword,
                            onValueChange = { viewModel.onConfirmPasswordInput(it) },
                            label = "Confirmar Contraseña",
                            icon = Icons.Outlined.Lock,
                            hideText = true,
                            errorMsg = viewModel.confirmPasswordErrMsg,
                            validateField = { viewModel.validateConfirmPassword() }
                        )

                        DefaultButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            text = "REGISTRARSE",
                            onClick = { viewModel.onSignup() },
                            color = Color(0xFFE57373), // Cambia el color del botón
                            enabled = viewModel.isEnabledLoginButton
                        )

                    }
                }

            }
        }

    }
}