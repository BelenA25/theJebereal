package com.jebereal.jeberealapp.concertApp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: FirebaseConcertApi.ConcertViewModel = hiltViewModel(),
    navController: NavHostController
) {
    var searchQuery by remember { mutableStateOf("") }
    var filteredConcerts by remember { mutableStateOf<List<Concert>>(emptyList()) }

    // Obtener todos los conciertos
    val allConcerts by viewModel.popularConcerts.collectAsState()

    // Filtrar conciertos cuando cambia la consulta de búsqueda
    LaunchedEffect(searchQuery) {
        filteredConcerts = allConcerts.filter { concert ->
            concert.name.contains(searchQuery, ignoreCase = true) ||
                    concert.genre.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        // Barra de búsqueda
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp) // Espacio desde la parte superior para evitar que choque
                .shadow(4.dp, shape = RoundedCornerShape(8.dp)) // Sombra y borde redondeado
                .background(Color.White, shape = RoundedCornerShape(8.dp)) // Fondo blanco con borde redondeado
                .height(48.dp), // Altura reducida
            placeholder = {
                Text(
                    text = "Buscar conciertos...",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp) // Ajuste de tamaño de texto del placeholder
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            },
            shape = RoundedCornerShape(8.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp), // Ajuste de tamaño de texto
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White, // Fondo blanco del TextField
                focusedLabelColor = Color.Black, // Color de la etiqueta cuando está enfocado
                unfocusedLabelColor = Color.Black, // Color de la etiqueta cuando no está enfocado
                focusedIndicatorColor = Color.Transparent, // Sin indicador cuando está enfocado
                unfocusedIndicatorColor = Color.Transparent // Sin indicador cuando no está enfocado
            )
        )
        // Mostrar resultados de búsqueda
        if (searchQuery.isEmpty()) {
            // Mostrar contenido similar a ConcertApp cuando no hay búsqueda
            ConcertAppContent(viewModel, navController)
        } else {
            // Mostrar resultados filtrados
            if (filteredConcerts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron conciertos",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn {
                    items(filteredConcerts) { concert ->
                        ConcertItemHorizontal(concert) { concertId ->
                            navController.navigate("concert_detail/$concertId")
                        }
                    }
                }
            }
        }
    }
}

// Extraer el contenido de ConcertApp en un componente separado para reutilización
@Composable
fun ConcertAppContent(
    viewModel: FirebaseConcertApi.ConcertViewModel,
    navController: NavHostController
) {
    val popularConcerts by viewModel.popularConcerts.collectAsState()
    val bestOffersConcerts by viewModel.bestOffersConcerts.collectAsState()
    val calendarConcerts by viewModel.calendarConcerts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var showAllPopularConcerts by remember { mutableStateOf(false) }
    var showAllBestOffersConcerts by remember { mutableStateOf(false) }
    var showAllCalendarConcerts by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Mostrar errores
            error?.let { errorMessage ->
                item {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            // Sección de Conciertos Populares
            item { SectionTitle("Conciertos Populares") }
            items(if (showAllPopularConcerts) popularConcerts else popularConcerts.take(2)) { concert ->
                ConcertItemHorizontal(concert) { concertId ->
                    navController.navigate("concert_detail/$concertId")
                }
            }

            // Botón Mostrar más conciertos populares
            item {
                if (!showAllPopularConcerts) {
                    Button(
                        onClick = { showAllPopularConcerts = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), // Sin fondo
                        border = BorderStroke(1.dp, Color.Black), // Borde negro
                        shape = RoundedCornerShape(16.dp) // Borde más redondeado
                    ) {
                        Text(
                            text = "Mostrar más conciertos",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red) // Letra roja
                        )
                    }
                }
            }

            // Espacio entre secciones
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Sección de Mejores Ofertas
            item { SectionTitle("Mejores Ofertas") }
            items(if (showAllBestOffersConcerts) bestOffersConcerts else bestOffersConcerts.take(2)) { concert ->
                ConcertItemHorizontal(concert) { concertId ->
                    navController.navigate("concert_detail/$concertId")
                }
            }

            // Botón Mostrar más ofertas
            item {
                if (!showAllBestOffersConcerts) {
                    Button(
                        onClick = { showAllBestOffersConcerts = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), // Sin fondo
                        border = BorderStroke(1.dp, Color.Black), // Borde negro
                        shape = RoundedCornerShape(16.dp) // Borde más redondeado
                    ) {
                        Text(
                            text = "Mostrar más ofertas",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red) // Letra roja
                        )
                    }
                }
            }

            // Espacio entre secciones
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Sección de Calendario de Conciertos
            item { SectionTitle("Calendario de Conciertos") }
            items(if (showAllCalendarConcerts) calendarConcerts else calendarConcerts.take(2)) { concert ->
                ConcertItemHorizontal(concert) { concertId ->
                    navController.navigate("concert_detail/$concertId")
                }
            }

            // Botón Mostrar más conciertos del calendario
            item {
                if (!showAllCalendarConcerts) {
                    Button(
                        onClick = { showAllCalendarConcerts = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), // Sin fondo
                        border = BorderStroke(1.dp, Color.Black), // Borde negro
                        shape = RoundedCornerShape(16.dp) // Borde más redondeado
                    ) {
                        Text(
                            text = "Mostrar más conciertos",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red) // Letra roja
                        )
                    }
                }
            }

            // Espacio adicional al final
            item { Spacer(modifier = Modifier.height(180.dp)) }
        }
    }
}



@Composable
fun ConcertItemHorizontal(concert: Concert, onClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(concert.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del concierto
        AsyncImage(
            model = concert.imageUrl,
            contentDescription = "Imagen de ${concert.name}",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Información del concierto
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Fecha
            Text(
                text = concert.date,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black // Fecha en color negro
            )
            // Título
            Text(
                text = concert.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1
            )
            // Género y precio juntos
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Género con ancho fijo
                Row(
                    modifier = Modifier.width(120.dp), // Ancho fijo para el género
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote, // Ícono de música
                        contentDescription = "Género musical",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = concert.genre,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                // Precio con ícono de ticket
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ConfirmationNumber, // Ícono de ticket
                        contentDescription = "Precio",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Bs. ${concert.price}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

