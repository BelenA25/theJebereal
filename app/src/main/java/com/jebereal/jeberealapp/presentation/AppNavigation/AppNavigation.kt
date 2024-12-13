package com.jebereal.jeberealapp.presentation.AppNavigation


import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jebereal.jeberealapp.BottomNavigationBar

import com.jebereal.jeberealapp.NavigationItem
import com.jebereal.jeberealapp.concertApp.ConcertApp
import com.jebereal.jeberealapp.concertApp.ConcertDetailScreen
import com.jebereal.jeberealapp.concertApp.FirebaseConcertApi

import com.jebereal.jeberealapp.concertApp.PaymentScreen
import com.jebereal.jeberealapp.concertApp.PurchasedTicketsDetailScreen
import com.jebereal.jeberealapp.concertApp.SearchScreen
import com.jebereal.jeberealapp.concertApp.TicketOptionsScreen
import com.jebereal.jeberealapp.concertApp.TicketPurchaseScreen
import com.jebereal.jeberealapp.presentation.screens.PaginaPrincipal.WelcomeScreen
import com.jebereal.jeberealapp.presentation.screens.login.LoginScreen
import com.jebereal.jeberealapp.presentation.screens.login.LoginViewModel
import com.jebereal.jeberealapp.presentation.screens.profile.ProfileScreen
import com.jebereal.jeberealapp.presentation.screens.signup.SignupScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainNavigation(navController: NavHostController) {
    val innerNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = innerNavController)
        }
    ) {
        NavHost(
            navController = innerNavController,
            startDestination = NavigationItem.Home.route
        ) {
            composable(NavigationItem.Home.route) {
                HomeScreen(navController)
            }
            composable(NavigationItem.Search.route) {
                SearchScreenAll(navController = navController)
            }
            composable(NavigationItem.Tickets.route) {
                TicketsScreen(navController)
            }
            composable(NavigationItem.Profile.route) {
                ProfileScreenNav(navController)
            }
        }
    }
}





@Composable
fun HomeScreen(navController: NavHostController) {
    ConcertApp(viewModel = hiltViewModel(), navController = navController)
}

@Composable
fun SearchScreenAll(navController: NavHostController) {
    // Aquí puedes poner el contenido para la pantalla de búsqueda
    //Text("Search Screen")
    SearchScreen(navController = navController)
}

@Composable
fun TicketsScreen(navController: NavHostController) {
    // Aquí puedes poner el contenido para la pantalla de tickets
    //Text("Tickets Screen")
   // Text("hello world")
   TicketPurchaseScreen(navController)
}

@Composable
fun ProfileScreenNav(navController: NavHostController) {
    // Aquí puedes poner el contenido para la pantalla de perfil
    //Text("Profile Screen")
    ProfileScreen(navController)
}





@Composable
fun AppNavigation(navController: NavHostController) {
    val viewModelConcert: FirebaseConcertApi.ConcertViewModel = hiltViewModel()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val currentUser = loginViewModel.currentUser

    // Determinar la pantalla inicial según el estado del usuario
    val startDestination = if (currentUser != null) {
        AppScreen.Main.route // Usuario logueado
    } else {
        AppScreen.Welcome.route // Usuario no logueado
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = AppScreen.Welcome.route) {
            WelcomeScreen(navController)
        }
        // Pantallas principales
        composable(route = AppScreen.Login.route) {
            LoginScreen(navController)
        }

        composable(route = AppScreen.Signup.route) {
           SignupScreen(navController)
        }

        composable(route = AppScreen.Profile.route) {
            ProfileScreen(navController)
        }

        composable(route = AppScreen.Main.route) {
            MainNavigation(navController)
        }

        // Detalle del concierto
        composable(
            route = "concert_detail/{concertId}",
            arguments = listOf(navArgument("concertId") { type = NavType.StringType })
        ) { backStackEntry ->
            val concertId = backStackEntry.arguments?.getString("concertId") ?: return@composable
            ConcertDetailScreen(
                concertId = concertId,
                navController = navController,
                viewModel = viewModelConcert
            )
        }

        // Opciones de tickets
        composable(
            route = "ticket_options/{concertId}",
            arguments = listOf(navArgument("concertId") { type = NavType.StringType })
        ) { backStackEntry ->
            val concertId = backStackEntry.arguments?.getString("concertId") ?: return@composable
            TicketOptionsScreen(
                concertId = concertId,
                viewModelConcert = viewModelConcert,
                navController
            )
        }

        // Pantalla de pago
        composable(
            route = "payment/{totalAmount}",
            arguments = listOf(navArgument("totalAmount") { type = NavType.StringType })
        ) { backStackEntry ->
            val totalAmount = backStackEntry.arguments?.getString("totalAmount")?.toDoubleOrNull() ?: 0.0
            PaymentScreen(
                totalAmount = totalAmount,
                onPay = {
                    // Volver a la pantalla principal tras el pago exitoso
                    navController.navigate(AppScreen.Main.route) {
                        popUpTo(AppScreen.Main.route) { inclusive = true }
                    }
                }
            )
        }


        // In AppNavigation
        composable(
            route = "purchased_tickets/{concertId}",
            arguments = listOf(navArgument("concertId") { type = NavType.StringType })
        ) { backStackEntry ->
            val concertId = backStackEntry.arguments?.getString("concertId") ?: return@composable
            PurchasedTicketsDetailScreen(
                concertId = concertId,
                navController = navController
            )
        }
    }
}




