package com.jebereal.jeberealapp.presentation.screens.concertApp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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