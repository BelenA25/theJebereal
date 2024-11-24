package com.example.myapplication.concertApp

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage




@Composable
fun ConcertApp(viewModel: ConcertViewModel, navController: NavHostController) {
    val popularConcerts by viewModel.popularConcerts.collectAsState()
    val bestOffersConcerts by viewModel.bestOffersConcerts.collectAsState()
    val calendarConcerts by viewModel.calendarConcerts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Sección de Conciertos Populares
            SectionTitle("Conciertos Populares")
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(vertical = 8.dp)
            ) {
                items(popularConcerts) { concert ->
                    ConcertItem(concert) { concertId ->
                        navController.navigate("concert_detail/$concertId")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sección de Mejores Ofertas
            SectionTitle("Mejores Ofertas")
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(vertical = 8.dp)
            ) {
                items(bestOffersConcerts) { concert ->
                    ConcertItem(concert) { concertId ->
                        navController.navigate("concert_detail/$concertId")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sección de Calendario de Conciertos
            SectionTitle("Calendario de Conciertos")
            ExpandableConcertList(
                concerts = calendarConcerts,
                onConcertClick = { concertId ->
                    navController.navigate("concert_detail/$concertId")
                }
            )

            /*calendarConcerts.forEach { concert ->
                ConcertItem(
                    concert = concert,
                    onClick = { concertId ->
                        navController.navigate("concert_detail/$concertId")
                    },

                )
            }*/

            // Espacio adicional al final para evitar que el último elemento
            // quede oculto por la barra de navegación
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}


@Composable
fun ExpandableConcertList(
    concerts: List<Concert>,
    onConcertClick: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val displayConcerts = if (isExpanded) concerts else concerts.take(2)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Mostrar los conciertos (2 o todos dependiendo del estado)
        displayConcerts.forEach { concert ->
            ConcertItemCalendar(
                concert = concert,
                onClick = onConcertClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }

        // Mostrar el botón "Ver más" solo si hay más de 2 conciertos
        if (concerts.size > 2) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { isExpanded = !isExpanded },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isExpanded) "Ver menos" else "Ver ${concerts.size - 2} conciertos más",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Icon(
                        imageVector = if (isExpanded)
                            Icons.Default.KeyboardArrowUp
                        else
                            Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Colapsar" else "Expandir",
                        modifier = Modifier.padding(start = 8.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun ConcertItemCalendar(
    concert: Concert,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick(concert.id) }
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del concierto
            AsyncImage(
                model = concert.imageUrl,
                contentDescription = "Imagen de ${concert.name}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            // Información del concierto
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = concert.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Fecha: ${concert.date}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Género: ${concert.genre}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Precio y descuento
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "$${concert.price}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                concert.discount?.let {
                    Text(
                        text = "-$it%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun ConcertsRow(concerts: List<Concert>, navController: NavHostController) {
    if (concerts.isEmpty()) {
        Text(
            text = "No hay conciertos disponibles",
            modifier = Modifier.padding(vertical = 8.dp)
        )
    } else {
        LazyRow(
            modifier = Modifier.height(220.dp)
        ) {
            items(concerts) { concert ->
                ConcertItem(concert) { concertId ->
                    // Navegar a la pantalla de detalles del concierto
                    navController.navigate("concert_detail/$concertId")
                }
            }
        }
    }
}

@Composable
fun ConcertItem(concert: Concert, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .width(250.dp)
            .padding(horizontal = 8.dp)
            .clickable { onClick(concert.id) }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = concert.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Género: ${concert.genre}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Fecha: ${concert.date}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Precio: $${concert.price}",
                style = MaterialTheme.typography.bodyMedium
            )
            concert.discount?.let {
                Text(
                    text = "Descuento: $it%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Imagen con Coil
            AsyncImage(
                model = concert.imageUrl,
                contentDescription = "Imagen de ${concert.name}",
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}


@Composable
fun ConcertDetailScreen(concertId: String, navController: NavHostController, viewModel: ConcertViewModel) {
    val concert by viewModel.loadConcertDetails(concertId).collectAsState(initial = null)

    concert?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = it.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "Fecha: ${it.date}")
            Text(text = "Descripción: ${it.venue}")

            Button(onClick = {
                navController.navigate("ticket_options/$concertId")
            }) {
                Text("Comprar Entrada")
            }
        }
    } ?: run {
        CircularProgressIndicator()
    }
}


@Composable
fun TicketOptionsScreen(concertId: String, viewModelConcert: ConcertViewModel, onCheckout: (Double) -> Unit) {
    val tickets by viewModelConcert.loadTicketsForConcert(concertId).collectAsState(initial = emptyList())

    // Estados para el total y la cantidad de tickets seleccionados
    var totalAmount by remember { mutableStateOf(0.0) }
    var totalSelectedTickets by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Opciones de Tickets para el Concierto ID: $concertId", style = MaterialTheme.typography.titleLarge)

        // Agrupar por tipo y mostrar la cantidad y precio
        val groupedTickets = tickets.groupBy { it.type }

        groupedTickets.forEach { (type, ticketList) ->
            val availableTickets = ticketList.filter { it.available }
            if (availableTickets.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "$type (${availableTickets.size} disponibles)", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(8.dp))

                    // Botón para seleccionar el tipo de ticket
                    Button(onClick = {
                        // Seleccionar un ticket disponible y actualizar el total y la cantidad
                        val selectedTicket = availableTickets.firstOrNull()
                        if (selectedTicket != null) {
                            // Marcar ticket como no disponible y actualizar el estado en el ViewModel
                            selectedTicket.available = false
                            totalAmount += selectedTicket.price
                            totalSelectedTickets += 1
                        }
                    }) {
                        Text(text = "Seleccionar")
                    }
                }

                availableTickets.forEach { ticket ->
                    Text(text = "Asiento ${ticket.seatNumber} - Precio: ${ticket.price}€")
                }
            } else {
                Text(text = "$type (No hay disponibles)")
            }
        }

        // Mostrar el total acumulado y la cantidad de tickets seleccionados al final de la pantalla
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Cantidad de tickets seleccionados: $totalSelectedTickets", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Total acumulado: $totalAmount€", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onCheckout(totalAmount) },
            enabled = totalSelectedTickets > 0
        ) {
            Text("Finalizar Compra")
        }
    }
}

@Composable
fun PaymentScreen(totalAmount: Double, onPay: () -> Unit) {
    var cardNumber by remember { mutableStateOf("") }
    var cardHolderName by remember { mutableStateOf("") }

    // Obtener el contexto actual
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Total: $$totalAmount",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Card Number") },
            modifier = Modifier
                .fillMaxWidth()
                .focusable(true)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = cardHolderName,
            onValueChange = { cardHolderName = it },
            label = { Text("Cardholder Name") },
            modifier = Modifier
                .fillMaxWidth()
                .focusable(true)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
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

