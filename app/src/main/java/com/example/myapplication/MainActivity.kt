package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.myapplication.UserApp.LoginUiState
import com.example.myapplication.UserApp.UserSessionViewModel
import com.example.myapplication.concertApp.Concert
import com.example.myapplication.concertApp.ConcertViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
class MainActivity : ComponentActivity() {
    private val viewModel: UserSessionViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UserSessionViewModel(application) as T
            }
        }
    }

    private val viewModelConcert: ConcertViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ConcertViewModel() as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MyApp(viewModel, viewModelConcert)
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object ConcertMain : Screen("concert_main")
}



sealed class NavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : NavigationItem("home", "Home", Icons.Default.Home)
    object Search : NavigationItem("search", "Search", Icons.Default.Search)
    object Tickets : NavigationItem("tickets", "Tickets", Icons.Default.Person)
    object Profile : NavigationItem("profile", "Profile", Icons.Default.Person)
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Search,
        NavigationItem.Tickets,
        NavigationItem.Profile
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileScreen(viewModel: UserSessionViewModel, onLogout: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Perfil",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        when (uiState) {
            is LoginUiState.Success -> {
                val user = (uiState as LoginUiState.Success).user
                Text("Email: ${user.email}")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onLogout) {
                    Text("Cerrar Sesión")
                }
            }
            else -> {
                CircularProgressIndicator()
            }
        }
    }
}



@Composable
fun MyApp(viewModel: UserSessionViewModel, viewModelConcert: ConcertViewModel) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    // Observar cambios en el estado de autenticación
    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.Initial, is LoginUiState.Error -> {
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            is LoginUiState.Success -> {
                if (navController.currentDestination?.route == Screen.Login.route) {
                    navController.navigate(NavigationItem.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
            else -> {}
        }
    }

    Scaffold(
        bottomBar = {
            // Solo mostrar la barra de navegación si el usuario está autenticado
            if (uiState is LoginUiState.Success) {
                BottomNavigation(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(viewModel)
            }

            composable(NavigationItem.Home.route) {
                // Verificar si el usuario está autenticado
                if (uiState is LoginUiState.Success) {
                    ConcertApp(viewModelConcert, navController)
                }
            }

            composable(NavigationItem.Search.route) {
                if (uiState is LoginUiState.Success) {
                    Text("Search Screen")
                }
            }

            composable(NavigationItem.Tickets.route) {
                if (uiState is LoginUiState.Success) {
                    Text("Tickets Screen")
                }
            }

            composable(NavigationItem.Profile.route) {
                if (uiState is LoginUiState.Success) {
                    ProfileScreen(
                        viewModel = viewModel,
                        onLogout = {
                            viewModel.logout()
                        }
                    )
                }
            }


            composable("concert_detail/{concertId}") { backStackEntry ->
                val concertId = backStackEntry.arguments?.getString("concertId") ?: return@composable
                ConcertDetailScreen(concertId = concertId, navController = navController, viewModelConcert)
            }

            composable("ticket_options/{concertId}") { backStackEntry ->
                val concertId = backStackEntry.arguments?.getString("concertId") ?: return@composable
                TicketOptionsScreen(concertId = concertId, viewModelConcert,onCheckout = { totalAmount ->
                    // Cuando el usuario finaliza la compra, navega a la pantalla de pago
                    navController.navigate("payment/$totalAmount")
                })
            }


            composable("payment/{totalAmount}") { backStackEntry ->
                val totalAmount = backStackEntry.arguments?.getString("totalAmount")?.toDoubleOrNull() ?: 0.0
                PaymentScreen(totalAmount = totalAmount, onPay = {
                    // Aquí procesarías el pago o mostrarías un mensaje de confirmación
                })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegistrationScreen(viewModel: UserSessionViewModel, navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("REGISTER") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


        }
    }
}


/*@Composable
fun MainScreen(viewModel: UserSessionViewModel, navController: NavHostController) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is LoginUiState.Success -> {
            //Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
            navController.navigate("concertMain")
        }
        is LoginUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }
        else -> {
            LoginScreen(viewModel)
        }
    }
}*/


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: UserSessionViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    // Background color similar to the image
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAD4D3)) // Fondo de la parte superior
    ) {
        // Parte superior naranja
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Altura de la parte superior
                .background(Color(0xFFE57373)) // Color naranja
        ) {
            // Título en la parte superior
            Text(
                text = "Inicia sesión",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        // Parte blanca inferior
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp) // Bordes redondeados solo arriba
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.Top, // Alineado al inicio
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(35.dp))

            // TextField de Gmail
            TextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(
                        "Gmail:",
                        fontSize = 25.sp, // Tamaño más grande para el título "Gmail"
                        fontWeight = FontWeight.Normal // Opción de ponerlo en negrita si lo deseas
                    )
                },
                modifier = Modifier
                    .padding(bottom = 25.dp), // Reduce el padding entre los campos
                singleLine = true,
                textStyle = LocalTextStyle.current.copy( // Ajuste de tamaño de fuente del texto ingresado
                    fontSize = 18.sp // Aumentar tamaño de fuente del input
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )


            TextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(
                        "Contraseña:",
                        fontSize = 25.sp, // Tamaño más grande para el título "Contraseña"
                        fontWeight = FontWeight.Normal // Opción de ponerlo en negrita si lo deseas
                    )
                },
                modifier = Modifier
                    .padding(bottom = 25.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                textStyle = LocalTextStyle.current.copy( // Ajuste de tamaño de fuente del texto ingresado
                    fontSize = 18.sp // Aumentar tamaño de fuente del input
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )
            Spacer(modifier = Modifier.height(25.dp))
            // Texto de "¿Olvidaste tu contraseña?"
            Text(
                text = "¿Olvidaste tu contraseña?",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF757575),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 8.dp, bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(150.dp))
            // Botón de inicio de sesión
            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier
                    .fillMaxWidth() // Ocupa todo el ancho disponible
                    .padding(horizontal = 60.dp) // Agrega espacio a los lados
                    .shadow(8.dp, shape = RoundedCornerShape(50)), // Sombra y esquinas redondeadas
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE57373),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50)
            ) {
                Text("Inicia Sesión")
            }


            // Mostrar errores o estado de carga
            when (uiState) {
                is LoginUiState.Error -> {
                    Text(
                        text = (uiState as LoginUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                is LoginUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                else -> {}
            }
        }
    }
}




@Composable
fun ConcertApp(viewModel: ConcertViewModel, navController: NavHostController) {
    val popularConcerts by viewModel.popularConcerts.collectAsState()
    val bestOffersConcerts by viewModel.bestOffersConcerts.collectAsState()
    val calendarConcerts by viewModel.calendarConcerts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        error?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Popular Concerts Section
        SectionTitle("Conciertos Populares")
        ConcertsRow(concerts = popularConcerts, navController)

        // Best Offers Section
        SectionTitle("Mejores Ofertas")
        ConcertsRow(concerts = bestOffersConcerts, navController)

        // Calendar Section
        SectionTitle("Calendario de Conciertos")
        ConcertsRow(concerts = calendarConcerts, navController)
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

