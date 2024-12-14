package com.jebereal.jeberealapp.presentation.screens.concertApp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.jebereal.jeberealapp.core.Concert
import kotlinx.coroutines.tasks.await


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchasedTicketsDetailScreen(
    concertId: String,
    navController: NavHostController,
    viewModel: ConcertViewModel = hiltViewModel() // Use ViewModel
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

