package com.jebereal.jeberealapp.presentation.screens.concertApp

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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.jebereal.jeberealapp.R
import com.jebereal.jeberealapp.util.showNotification
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

fun formatDate(dateString: String): String {
    // Suponiendo que la fecha está en formato "YYYY-MM-DD" como en el ejemplo 2024-09-10
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
    val date = inputFormat.parse(dateString)
    return outputFormat.format(date)
}

// Funciones de extensión para formatear fecha
fun String.formatMonth(): String {
    val sdf = SimpleDateFormat("MMMM", Locale.getDefault())
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this)
    return sdf.format(date ?: "").replaceFirstChar { it.uppercase() }
}

fun String.formatDay(): String {
    val sdf = SimpleDateFormat("d", Locale.getDefault())
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this)
    return sdf.format(date ?: "")
}

fun String.formatDayOfWeek(): String {
    val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this)
    val fullDayOfWeek = sdf.format(date ?: "").replaceFirstChar { it.uppercase() }
    return fullDayOfWeek.take(4) // Tomar solo las primeras 4 letras
}

/*
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
*/




