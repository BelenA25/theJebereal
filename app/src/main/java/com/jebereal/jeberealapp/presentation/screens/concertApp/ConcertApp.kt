package com.jebereal.jeberealapp.presentation.screens.concertApp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jebereal.jeberealapp.data.repository.FirebaseConcertApi
import com.jebereal.jeberealapp.presentation.screens.concertApp.ConcertViewModel
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

            // Espacio adicional al final para evitar que el último elemento
            // quede oculto por la barra de navegación
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}