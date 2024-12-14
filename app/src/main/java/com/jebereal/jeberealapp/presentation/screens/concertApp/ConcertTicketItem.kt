package com.jebereal.jeberealapp.presentation.screens.concertApp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jebereal.jeberealapp.core.Concert
import com.jebereal.jeberealapp.core.TicketConcert


@Composable
fun ConcertTicketItem(
    concert: Concert,
    transaction: Map<String, Any>,
    ticketsForConcert: List<TicketConcert>,
    onConcertClick: () -> Unit
) {
    // Card principal transparente
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onConcertClick),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent) // Transparente
    ) {
        // Subcard 1 ancho reducido
        val subCard1Width = 90.dp // Ancho fijo para la subcard 1
        // Mes centrado encima de la subcard 1 y alineado a la izquierda
        Box(
            modifier = Modifier
                .align(Alignment.Start) // Alineación a la izquierda
                .width(subCard1Width) // Ancho igual al de la subcard 1
                .padding(start = 10.dp, bottom = 10.dp) // Aumenta el bottom padding para mayor separación
        ) {
            Text(
                text = concert.date.formatMonth(),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 15.sp, // Más pequeño
                    fontWeight = FontWeight.Normal // Sin negrita
                ),
                textAlign = TextAlign.Start, // Alineado a la izquierda
                modifier = Modifier.fillMaxWidth() // Asegura que ocupe todo el ancho disponible
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp) // Espacio dentro de la card
        ) {
            // Subtarjeta 1: Día y día de la semana
            Card(
                modifier = Modifier
                    .width(56.dp)
                    .height(60.dp) // Asegura suficiente altura para los textos
                    .padding(end = 8.dp), // Espacio entre las subcards
                shape = RoundedCornerShape(8.dp), // Bordes redondeados
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Sin elevación
                colors = CardDefaults.cardColors(containerColor = Color.White) // Fondo blanco
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp), // Reducir padding interno
                    horizontalAlignment = Alignment.CenterHorizontally, // Centrado horizontalmente
                    verticalArrangement = Arrangement.Center // Centrado verticalmente
                ) {
                    Text(
                        text = concert.date.formatDay(),
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontSize = 20.sp, // Tamaño ajustado
                            fontWeight = FontWeight.Bold, // Día en negrita
                            lineHeight = 20.sp // Elimina espacio adicional
                        ),
                        maxLines = 1 // Asegura que el texto esté en una sola línea
                    )
                    Text(
                        text = concert.date.formatDayOfWeek(),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp, // Tamaño pequeño para que encaje
                            color = Color.Gray, // Color gris
                            lineHeight = 10.sp // Elimina espacio adicional
                        ),
                        modifier = Modifier.padding(top = 0.dp), // Casi sin espacio entre textos
                        maxLines = 1 // Asegura que el texto esté en una sola línea
                    )
                }
            }

            // Subtarjeta 2: Título y Precio
            Card(
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(8.dp), // Bordes redondeados
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Sin elevación
                colors = CardDefaults.cardColors(containerColor = Color.Transparent) // Fondo transparente
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight().padding(5.dp), // Asegura que la columna ocupe toda la altura disponible
                    verticalArrangement = Arrangement.Center // Centra verticalmente los elementos dentro de la columna
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Título del concierto
                        Text(
                            text = concert.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold // Negrita
                            ),
                            modifier = Modifier.weight(1f) // Ocupa el espacio restante
                        )

                        // Precio
                        Text(
                            text = "${transaction["amount"]} Bs.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White), // Precio en blanco
                            textAlign = TextAlign.End
                        )
                    }

                    // Hora
                    Text(
                        text = "19:00 - 22:00",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

