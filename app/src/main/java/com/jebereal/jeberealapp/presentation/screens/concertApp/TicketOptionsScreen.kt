package com.jebereal.jeberealapp.presentation.screens.concertApp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jebereal.jeberealapp.core.TicketConcert


@Composable
fun TicketOptionsScreen(
    concertId: String,
    viewModelConcert: ConcertViewModel,
    navController: NavHostController
) {
    val tickets by viewModelConcert.loadTicketsForConcert(concertId).collectAsState(initial = emptyList())

    // Estados para el seguimiento de tickets seleccionados
    var selectedTickets by remember { mutableStateOf<List<TicketConcert>>(emptyList()) }

    // Estado para manejar la disponibilidad de tickets
    var areAllTicketsAvailable by remember { mutableStateOf(true) }

    // Contexto para mostrar toast
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Selección de Tickets",
            style = MaterialTheme.typography.titleLarge
        )

        // Agrupar tickets por tipo
        val groupedTickets = tickets.groupBy { it.type }

        groupedTickets.forEach { (type, ticketList) ->
            val availableTickets = ticketList.filter { it.available }

            // Sección para cada tipo de ticket
            Text(
                text = "$type Tickets",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp)
            )

            availableTickets.forEach { ticket ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            // Lógica para seleccionar/deseleccionar ticket
                            selectedTickets = if (selectedTickets.contains(ticket)) {
                                selectedTickets.filter { it != ticket }
                            } else {
                                selectedTickets + ticket
                            }
                        }
                        .background(
                            color = if (selectedTickets.contains(ticket))
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                Color.Transparent
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Asiento ${ticket.seatNumber} - Fila ${ticket.row}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Precio: $${ticket.price}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Checkbox(
                        checked = selectedTickets.contains(ticket),
                        onCheckedChange = {
                            selectedTickets = if (it) {
                                selectedTickets + ticket
                            } else {
                                selectedTickets.filter { t -> t != ticket }
                            }
                        }
                    )
                }
            }
        }

        // Resumen de selección
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tickets seleccionados: ${selectedTickets.size}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Total: $${selectedTickets.sumOf { it.price }}",
            style = MaterialTheme.typography.bodyLarge
        )

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
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Continuar a Pago")
        }

    }
}

