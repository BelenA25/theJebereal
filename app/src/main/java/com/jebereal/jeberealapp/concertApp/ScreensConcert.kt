package com.jebereal.jeberealapp.concertApp

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.tasks.await
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberImagePainter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ConcertApp(viewModel: FirebaseConcertApi.ConcertViewModel, navController: NavHostController) {
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
    Row(
        modifier = modifier
            .clickable { onClick(concert.id) }
            .padding(vertical = 4.dp)
    ) {
        val date = concert.date.split("-")
        val day = date.getOrNull(2) ?: ""
        val month = when (date.getOrNull(1)) {
            "01" -> "Enero"
            "02" -> "Febrero"
            "03" -> "Marzo"
            "04" -> "Abril"
            "05" -> "Mayo"
            "06" -> "Junio"
            "07" -> "Julio"
            "08" -> "Agosto"
            "09" -> "Septiembre"
            "10" -> "Octubre"
            "11" -> "Noviembre"
            "12" -> "Diciembre"
            else -> ""
        }

        Card(
            modifier = Modifier
                .width(80.dp)
                .height(130.dp)
                .padding(end = 16.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(2.dp, Color.Black),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Text(
                    text = day,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = month,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Black)
                )
            }
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                AsyncImage(
                    model = concert.imageUrl,
                    contentDescription = "Imagen de ${concert.name}",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White
                                )
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomStart)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp)
                            .wrapContentHeight(Alignment.Bottom)
                    ) {
                        Text(
                            text = concert.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = concert.date,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Género del concierto",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )

                        Text(
                            text = "$${concert.price}",
                            style = MaterialTheme.typography.titleMedium.copy(color = Color.Black)
                        )
                        concert.discount?.let {
                            Text(
                                text = "-$it%",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                            )
                        }
                    }
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
            .border(2.dp, Color.Black, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AsyncImage(
                model = concert.imageUrl,
                contentDescription = "Imagen de ${concert.name}",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Red.copy(alpha = 1f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = concert.date,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = concert.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "Género de música",
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = concert.genre,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "$${concert.price}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                concert.discount?.let {
                    Text(
                        text = "Descuento: $it%",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                    )
                }
            }
        }
    }
}
fun formatDate(dateString: String): String {
    // Suponiendo que la fecha está en formato "YYYY-MM-DD" como en el ejemplo 2024-09-10
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
    val date = inputFormat.parse(dateString)
    return outputFormat.format(date)
}

@Composable
fun ConcertDetailScreen(concertId: String, navController: NavHostController, viewModel: FirebaseConcertApi.ConcertViewModel) {
    val concert by viewModel.loadConcertDetails(concertId).collectAsState(initial = null)

    concert?.let {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = rememberImagePainter(it.imageUrl),
                contentDescription = "Imagen de fondo",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .clip(RectangleShape),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color(0xFFD1C171))
                    .padding(16.dp)
                    .height(400.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Start
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    val formattedDate = formatDate(it.date)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Fecha",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                            textAlign = TextAlign.Start
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Género",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "El género del concierto es: ${it.genre}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                            textAlign = TextAlign.Start
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = "Precio",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$${it.price}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(35.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${it.price}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    Button(
                        onClick = {
                            navController.navigate("ticket_options/$concertId")
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .width(200.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        border = BorderStroke(2.dp, Color.Black)
                    ) {
                        Text(
                            text = "Comprar Entrada",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                        )
                    }
                }
            }
        }
    } ?: run {
        CircularProgressIndicator()
        }
}


@Composable
fun TicketOptionsScreen(
    concertId: String,
    viewModelConcert: FirebaseConcertApi.ConcertViewModel,
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketPurchaseScreen(
    navController: NavHostController
) {
    var userTransactions by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var userConcerts by remember { mutableStateOf<List<Concert>>(emptyList()) }

    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            // Fetch user's transactions
            val transactionsSnapshot = FirebaseDatabase.getInstance().reference
                .child("transactions")
                .orderByChild("userId")
                .equalTo(userId)
                .get().await()

            userTransactions = transactionsSnapshot.children.mapNotNull {
                it.getValue<Map<String, Any>>()
            }

            // Fetch unique concert details for these transactions
            val concertIds = userTransactions.map { it["concertId"] as String }.distinct()
            userConcerts = concertIds.map { concertId ->
                FirebaseDatabase.getInstance().reference
                    .child("concerts")
                    .child(concertId)
                    .get().await()
                    .getValue(Concert::class.java)!!
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Concerts") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (userTransactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No tickets purchased", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(userConcerts) { concert ->
                    val concertTransactions = userTransactions.filter {
                        it["concertId"] as String == concert.id
                    }

                    ConcertTicketItem(
                        concert = concert,
                        transactionCount = concertTransactions.size,
                        onConcertClick = {
                            navController.navigate("purchased_tickets/${concert.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ConcertTicketItem(
    concert: Concert,
    transactionCount: Int,
    onConcertClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onConcertClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = concert.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Date: ${concert.date}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Tickets Purchased: $transactionCount",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchasedTicketsDetailScreen(
    concertId: String,
    navController: NavHostController,
    viewModel: FirebaseConcertApi.ConcertViewModel = hiltViewModel() // Use ViewModel
) {
    var purchasedTickets by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var concertDetails by remember { mutableStateOf<Concert?>(null) }

    // Collect tickets and concert details from ViewModel
    val ticketsForConcert by viewModel.ticketsForConcert.collectAsState()
    val concertDetail by viewModel.concertDetails.collectAsState()

    LaunchedEffect(concertId) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            // Load concert details and tickets for this specific concert
            viewModel.loadConcertDetails(concertId)
            viewModel.loadTicketsForConcert(concertId)

            // Fetch transactions for this concert and user
            val ticketsSnapshot = FirebaseDatabase.getInstance().reference
                .child("transactions")
                .orderByChild("userId")
                .equalTo(userId)
                .get().await()

            purchasedTickets = ticketsSnapshot.children
                .mapNotNull { it.getValue<Map<String, Any>>() }
                .filter { it["concertId"] as String == concertId }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Concert Tickets") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (purchasedTickets.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No tickets found", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                item {
                    concertDetail?.let { concert ->
                        Text(
                            text = concert.name,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Date: ${concert.date}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                items(purchasedTickets) { transaction ->
                    // Find the corresponding ticket details
                    val ticketDetails = ticketsForConcert.find {
                        it.idTicket == transaction["ticketId"]
                    }

                    // Pass ticket details to TicketDetailItem
                    TicketDetailItem(transaction, ticketDetails)
                }
            }
        }
    }
}

@Composable
fun TicketDetailItem(
    transaction: Map<String, Any>,
    ticket: TicketConcert?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Ticket ID
            Text(
                text = "Ticket ID: ${transaction["ticketId"]}",
                style = MaterialTheme.typography.bodyMedium
            )

            // Price
            Text(
                text = "Price: $${transaction["amount"]}",
                style = MaterialTheme.typography.bodyMedium
            )

            // Seat Number
            Text(
                text = "Seat Number: ${ticket?.seatNumber ?: "Not available"}",
                style = MaterialTheme.typography.bodyMedium
            )

            // Row
            Text(
                text = "Row: ${ticket?.row ?: "Not available"}",
                style = MaterialTheme.typography.bodyMedium
            )

            // Type
            Text(
                text = "Type: ${ticket?.type ?: "Not available"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
