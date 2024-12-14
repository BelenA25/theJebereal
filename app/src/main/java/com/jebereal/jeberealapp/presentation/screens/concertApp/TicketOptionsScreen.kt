package com.jebereal.jeberealapp.presentation.screens.concertApp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.jebereal.jeberealapp.R
import com.jebereal.jeberealapp.core.TicketConcert
import com.jebereal.jeberealapp.util.showNotification


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketOptionsScreen(
    concertId: String,
    viewModelConcert: ConcertViewModel,
    navController: NavHostController
) {
    val tickets by viewModelConcert.loadTicketsForConcert(concertId).collectAsState(initial = emptyList())

    // Estados para el seguimiento de tickets seleccionados
    var selectedTickets by remember { mutableStateOf<List<TicketConcert>>(emptyList()) }

    // Contexto para mostrar toast
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asientos") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Row1: Imagen y Cards
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp
            val imageWidth = screenWidth * 0.45f // 45% del ancho
            val imageHeight = imageWidth * 4f // Ajustar la altura de la imagen para hacerla más grande

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight) // Altura del Row
                    .padding(0.dp), // Sin padding
                verticalAlignment = Alignment.CenterVertically // Centrado vertical del contenido
            ) {
                // Aquí van las imágenes ajustadas al 45% del ancho de la pantalla, con altura proporcional
                val seatImages = listOf(R.drawable.seat1, R.drawable.seat2, R.drawable.seat3, R.drawable.seat4)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp) // Sin padding extra
                ) {
                    items(tickets) { ticket ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 30.dp) // Separación entre cada card
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp) // Altura más pequeña para cada tarjeta
                                    .clickable {
                                        selectedTickets = if (selectedTickets.contains(ticket)) {
                                            selectedTickets.filter { it != ticket }
                                        } else {
                                            selectedTickets + ticket
                                        }
                                    },
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Sin fondo ni sombra
                                shape = RectangleShape, // Sin bordes redondeados
                                colors = CardDefaults.cardColors(containerColor = Color.Transparent) // Fondo transparente
                            ) {
                                Row(
                                    modifier = Modifier.padding(0.dp), // Sin padding en todo el row
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Subcard 1: Imagen (pegada al borde izquierdo)
                                    Image(
                                        painter = painterResource(id = seatImages[tickets.indexOf(ticket) % seatImages.size]),
                                        contentDescription = "Imagen del asiento",
                                        modifier = Modifier
                                            .width(imageWidth)
                                            .height(imageHeight)
                                            .fillMaxHeight() // Asegúrate de que toque el borde superior e inferior
                                            .align(Alignment.CenterVertically), // Centra la imagen dentro del Row
                                        contentScale = ContentScale.Crop // Ajusta la escala
                                    )

                                    // Subcard 2: Ícono y Checkbox (con padding)
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 16.dp) // Padding a la izquierda
                                            .width(IntrinsicSize.Min) // Ajuste automático de ancho
                                    ) {
                                        val circleColors = listOf(
                                            Color(0xFFF44336), // Rojo, // Naranja
                                            Color(0xFF9C27B0),  // Morado
                                            Color(0xFF4CAF50), // Verde
                                            Color(0xFF2196F3)  // Azul
                                        )
                                        val circleColor = circleColors[tickets.indexOf(ticket) % circleColors.size] // Selección del color

                                        Icon(
                                            imageVector = Icons.Default.ConfirmationNumber, // Ícono de ticket
                                            contentDescription = "Ícono del ticket",
                                            modifier = Modifier
                                                .size(40.dp) // Tamaño del ícono
                                                .background(circleColor, shape = CircleShape) // Fondo circular con el color correspondiente
                                                .padding(8.dp), // Espaciado interno para centrar el ícono dentro del círculo
                                            tint = Color.White // Ícono blanco
                                        )

                                        // Checkbox debajo del ícono
                                        Checkbox(
                                            checked = selectedTickets.contains(ticket),
                                            onCheckedChange = {
                                                selectedTickets = if (it) {
                                                    selectedTickets + ticket
                                                } else {
                                                    selectedTickets.filter { t -> t != ticket }
                                                }
                                            },
                                            modifier = Modifier.padding(top = 8.dp) // Separación entre ícono y checkbox
                                        )
                                    }

                                    // Subcard 3: Información del ticket (con separación)
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 16.dp, end = 16.dp) // Separación de la subcard con el borde derecho
                                    ) {
                                        Text(
                                            text = "${ticket.type}".uppercase(),  // Convierte a mayúsculas
                                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold)
                                        )

                                        Text(
                                            text = "Asiento ${ticket.seatNumber} - Fila ${ticket.row}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )

                                        Text(
                                            text = "${ticket.price} Bs.",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontSize = 15.sp,  // Aumenta el tamaño de la letra
                                                fontWeight = FontWeight.Bold  // Puedes agregar negrita si lo deseas
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Row con los textos "Tickets" y "Total", y el botón
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 0.dp), // Reducir el espacio entre el row y el botón
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Columna con "Tickets" y "Total" debajo
                    Column(
                        modifier = Modifier
                            .padding(start = 32.dp) // Espaciado de la columna para centrar más los textos
                    ) {
                        Text(
                            text = "Tickets: ${selectedTickets.size}",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold, // Negrita
                                fontSize = 18.sp // Tamaño de letra
                            )
                        )

                        Text(
                            text = "Total: $${selectedTickets.sumOf { it.price }}",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold, // Negrita
                                fontSize = 18.sp // Tamaño de letra
                            )
                        )
                    }

                    // Botón de finalizar compra
                    Button(
                        onClick = {
                            if (selectedTickets.isNotEmpty()) {
                                val checkAvailability = selectedTickets.all { ticket ->
                                    tickets.find { it.idTicket == ticket.idTicket }?.available == true
                                }
                                if (checkAvailability) {
                                    // Crear transacción antes de navegar a la pantalla de pago
                                    viewModelConcert.createTransactionForTickets(
                                        concertId = concertId,
                                        selectedTickets = selectedTickets
                                    ) { error ->
                                        if (error == null) {
                                            // Actualizar disponibilidad y navegar
                                            viewModelConcert.updateTicketsAvailability(selectedTickets)
                                            val totalAmount = String.format("%.2f", selectedTickets.sumOf { it.price })
                                            navController.navigate("payment/$totalAmount")
                                            Toast.makeText(
                                                context,
                                                "Tickets seleccionados: ${selectedTickets.size}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Error creando transacción: $error",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Algunos tickets no disponibles. Seleccione nuevamente.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    viewModelConcert.loadTicketsForConcert(concertId)
                                    selectedTickets = emptyList()
                                }
                            }
                        },
                        enabled = selectedTickets.isNotEmpty(),
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .padding(top = 0.dp) // Reducir margen superior
                    ) {
                        Text("Continuar a Pago")
                    }
                }
            }

        }
    }
}






