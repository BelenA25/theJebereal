package com.example.myapplication.presentation.screens.PaginaPrincipal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import coil.compose.AsyncImage
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.presentation.AppNavigation.AppScreen

@Composable
fun WelcomeScreen(navController: NavHostController) {
    // Fondo degradado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFE2CC), // Beige claro
                        Color(0xFFF78A8A)  // Rojizo
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Título y mensaje de bienvenida
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(60.dp)) // Espacio para notch
                Text(
                    text = "ConcertsYA!",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp,
                                color = Color.Black
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Bienvenido de vuelta",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(vertical = 100.dp) // Aquí puedes ajustar el padding como desees
                )

            }

            // Botones de acción
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { navController.navigate(AppScreen.Login.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(200.dp)
                ) {
                    Text("Inicia sesión")
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { navController.navigate(AppScreen.Signup.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text("Regístrate")
                }
            }

            // Texto inferior y espacio para las siluetas
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "La música te conecta, los\nconciertos te elevan...",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                AsyncImage(
                    model = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.pngwing.com%2Fes%2Ffree-png-bsgzr&psig=AOvVaw06q1aTnHwRopCflQWQkDy2&ust=1733617865422000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCIilt_qzlIoDFQAAAAAdAAAAABAE", // Reemplaza con tu URL
                    contentDescription = "Siluetas de personas",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }
        }
    }
}
