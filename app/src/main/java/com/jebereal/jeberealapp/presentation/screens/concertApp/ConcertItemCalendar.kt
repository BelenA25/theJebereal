package com.jebereal.jeberealapp.presentation.screens.concertApp

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jebereal.jeberealapp.core.Concert


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

