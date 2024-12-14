package com.jebereal.jeberealapp.presentation.screens.concertApp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.jebereal.jeberealapp.R
import com.jebereal.jeberealapp.core.Concert
import kotlinx.coroutines.tasks.await


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketPurchaseScreen(
    navController: NavHostController,
    viewModel: ConcertViewModel = hiltViewModel() // Usamos el ViewModel
) {
    var userTransactions by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var userConcerts by remember { mutableStateOf<List<Concert>>(emptyList()) }

    // Obtener los tickets desde el ViewModel
    val ticketsForConcert by viewModel.ticketsForConcert.collectAsState()

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

    // Cambiar el degradado dependiendo de si hay tickets o no
    val gradientBrush = if (userTransactions.isEmpty()) {
        Brush.linearGradient(
            colors = listOf(Color(0xFF6F4F28), Color.White) // Degradado café a blanco
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color(0xFFC94140), Color.White) // Degradado de #C94140 a blanco
        )
    }

    // Scaffold con el degradado dinámico
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "My Concerts",
                            color = Color.White // Color blanco para el título
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White // Color blanco para la flecha
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent // Hacemos el fondo del TopAppBar transparente
                    ),
                    modifier = Modifier.background(Color.Transparent) // Asegura que el TopAppBar no tenga fondo
                )
            }
        },
        containerColor = Color.Transparent, // Fondo transparente para el Scaffold
        modifier = Modifier.background( // Aplica el degradado al fondo general del Scaffold
            gradientBrush
        )
    ) { padding ->
        if (userTransactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.Transparent), // Degradado café a blanco
                contentAlignment = Alignment.Center
            ) {
                // Imagen centrada en la parte superior
                Image(
                    painter = painterResource(id = R.drawable.no_tickets_purchased), // Reemplaza con el nombre de tu archivo PNG
                    contentDescription = "Imagen",
                    modifier = Modifier
                        .align(Alignment.TopCenter) // Centra la imagen en la parte superior
                        .size(400.dp) // Tamaño de la imagen (ajústalo según tus necesidades)
                )

                // Coloca el texto debajo de la imagen
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, // Centra los textos horizontalmente
                    verticalArrangement = Arrangement.Center // Centra los textos verticalmente
                ) {
                    Text(
                        "No tickets purchased",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold, // Texto en negrita
                            fontSize = 24.sp // Tamaño de texto más grande
                        ),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        "It looks like you haven't bought any tickets yet, don't you want to buy one?",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Normal, // Texto normal
                            fontSize = 16.sp, // Tamaño de texto más pequeño
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .fillMaxWidth() // Permite que el texto ocupe todo el ancho disponible
                            .padding(start = 30.dp, end = 30.dp, top = 15.dp) // Padding a los costados y arriba
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Subtítulo centrado, en negrita, blanco y tamaño grande
                Text(
                    "Your next concerts",
                    color = Color.White, // Color blanco
                    fontWeight = FontWeight.Medium, // Negrita
                    fontSize = 26.sp, // Tamaño grande
                    modifier = Modifier
                        .fillMaxWidth() // Hace que ocupe todo el ancho disponible
                        .padding(top = 20.dp), // Espacio arriba del subtítulo
                    textAlign = TextAlign.Center // Centra el texto horizontalmente
                )

                // Lista de conciertos debajo del subtítulo
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth() // Ocupa solo el ancho disponible
                        .padding(top = 16.dp) // Espacio arriba de la lista
                ) {
                    items(userConcerts) { concert ->
                        val concertTransactions = userTransactions.filter {
                            it["concertId"] as String == concert.id
                        }

                        ConcertTicketItem(
                            concert = concert,
                            transaction = concertTransactions.first(), // Pasa la transacción relevante
                            ticketsForConcert = ticketsForConcert, // Pasa la lista de tickets
                            onConcertClick = {
                                navController.navigate("purchased_tickets/${concert.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

