package com.jebereal.jeberealapp.presentation.screens.concertApp

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.FontWeight
import com.jebereal.jeberealapp.util.showNotification


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(totalAmount: Double, onPay: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var cvc by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    // Definición de colores
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFD67D79), // Color rojizo/rosado
            Color.White        // Blanco
        )
    )
    val cardColor = Color.White
    val accentColor = Color(0xFFE57373) // Color del botón
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush) // Fondo degradado
    ) {
        // Parte superior con el título y diseño solicitado
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PaymentTitle(totalAmount)
        }

        // Card del formulario
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 250.dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 60.dp, vertical = 24.dp)
                    .fillMaxWidth()
            ) {
                // Gmail Input
                Text("Gmail:", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = accentColor)
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("tugmail@gmail.com") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Información de la tarjeta
                Text("Información de la tarjeta:", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = accentColor)
                TextField(
                    value = cardNumber,
                    onValueChange = { cardNumber = it },
                    placeholder = { Text("Número de tarjeta") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
                )

                // MM/YY y CVC
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = expirationDate,
                        onValueChange = { expirationDate = it },
                        placeholder = { Text("MM/YY") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
                    )
                    TextField(
                        value = cvc,
                        onValueChange = { cvc = it },
                        placeholder = { Text("CVC") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // País o región
                Text("País o región:", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = accentColor)
                TextField(
                    value = country,
                    onValueChange = { country = it },
                    placeholder = { Text("Seleccione país") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
                )

                // Botón de Pagar
                Button(
                    onClick = {
                        showNotification(context)
                        // Mostrar un toast con el monto total y el mensaje "Success"
                        Toast.makeText(context, "Success! Total: $$totalAmount", Toast.LENGTH_LONG).show()
                        onPay()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pay Now")
                }
            }
        }
    }
}

@Composable
fun PaymentTitle(totalAmount: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$totalAmount Bs",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "Tickets de concierto",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                color = Color.Black,
                thickness = 1.dp,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
            Text(
                text = "Paga con tarjeta",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Divider(
                color = Color.Black,
                thickness = 1.dp,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}


