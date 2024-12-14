package com.jebereal.jeberealapp.presentation.screens.concertApp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jebereal.jeberealapp.core.Concert


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
