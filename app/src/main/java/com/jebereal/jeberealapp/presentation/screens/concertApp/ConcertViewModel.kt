package com.jebereal.jeberealapp.presentation.screens.concertApp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth

import com.jebereal.jeberealapp.core.Concert
import com.jebereal.jeberealapp.core.TicketConcert
import com.jebereal.jeberealapp.domain.repository.ConcertApi

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

// Definición del enum para géneros musicales
/*enum class MusicGenre {
    ROCK, POP, JAZZ, CLASSICAL, ELECTRONIC, HIP_HOP, REGGAETON, METAL
}*/

// Hilt Module for Dependency Injection

  /* abstract class ConcertApiModule {
        abstract fun bindConcertApi(
            firebaseConcertApi: FirebaseConcertApi
        ): ConcertApi

        companion object {

            fun provideFirebaseDatabase(): FirebaseDatabase {
                return Firebase.database
            }
        }
    }*/

    // ViewModel with Hilt
    @HiltViewModel
    class ConcertViewModel @Inject constructor(
        private val api: ConcertApi
    ) : ViewModel() {

        // State flows for concerts
        private val _popularConcerts = MutableStateFlow<List<Concert>>(emptyList())
        val popularConcerts: StateFlow<List<Concert>> = _popularConcerts.asStateFlow()

        private val _bestOffersConcerts = MutableStateFlow<List<Concert>>(emptyList())
        val bestOffersConcerts: StateFlow<List<Concert>> = _bestOffersConcerts.asStateFlow()

        private val _calendarConcerts = MutableStateFlow<List<Concert>>(emptyList())
        val calendarConcerts: StateFlow<List<Concert>> = _calendarConcerts.asStateFlow()

        private val _ticketsForConcert = MutableStateFlow<List<TicketConcert>>(emptyList())
        val ticketsForConcert: StateFlow<List<TicketConcert>> = _ticketsForConcert.asStateFlow()

        // State flows for loading and error
        private val _isLoading = MutableStateFlow(false)
        val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

        private val _error = MutableStateFlow<String?>(null)
        val error: StateFlow<String?> = _error.asStateFlow()

        // State flows for tickets and concert details
        private val _concertDetails = MutableStateFlow<Concert?>(null)
        val concertDetails: StateFlow<Concert?> = _concertDetails.asStateFlow()

        // Initialize data loading
        init {
            loadAllConcerts()
        }

        // Load all concert categories
        private fun loadAllConcerts() {
            viewModelScope.launch {
                _isLoading.value = true
                try {
                    _popularConcerts.value = api.getPopularConcerts()
                    _bestOffersConcerts.value = api.getBestOffersConcerts()
                    _calendarConcerts.value = api.getCalendarConcerts()
                    _error.value = null
                } catch (e: Exception) {
                    _error.value = "Error loading concerts: ${e.message}"
                    Log.e("ConcertViewModel", "Error loading concerts", e)
                } finally {
                    _isLoading.value = false
                }
            }
        }

        // Load tickets for a specific concert
        fun loadTicketsForConcert(concertId: String): StateFlow<List<TicketConcert>> {
            viewModelScope.launch {
                try {
                    // Filtrar tickets específicamente para este concierto
                    val tickets = api.getTicketsForConcert(concertId)
                    _ticketsForConcert.value = tickets.filter { it.concertId == concertId }
                } catch (e: Exception) {
                    _error.value = "Error loading tickets: ${e.message}"
                    Log.e("ConcertViewModel", "Error loading tickets", e)
                }
            }
            return ticketsForConcert
        }

        fun loadConcertDetails(concertId: String): StateFlow<Concert?> {
            viewModelScope.launch {
                try {
                    val concert = api.getConcertDetails(concertId)
                    _concertDetails.value = concert
                } catch (e: Exception) {
                    _error.value = "Error loading concert details: ${e.message}"
                    Log.e("ConcertViewModel", "Error loading concert details", e)
                }
            }
            return concertDetails
        }

        // Optional: Method to refresh all concerts
        fun refreshConcerts() {
            loadAllConcerts()
        }


        fun updateTicketsAvailability(tickets: List<TicketConcert>) {
            viewModelScope.launch {
                try {
                    tickets.forEach { ticket ->
                        api.updateTicketAvailability(ticket.idTicket, false)
                    }
                    // Opcional: recargar los tickets para reflejar el nuevo estado
                    loadTicketsForConcert(tickets.first().concertId)
                } catch (e: Exception) {
                    _error.value = "Error updating ticket availability: ${e.message}"
                    Log.e("ConcertViewModel", "Error updating tickets", e)
                }
            }
        }



        fun createTransactionForTickets(
            concertId: String,
            selectedTickets: List<TicketConcert>,
            onComplete: (String?) -> Unit
        ) {
            viewModelScope.launch {
                try {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                        ?: throw Exception("User not authenticated")
                    selectedTickets.forEach { ticket ->
                        val transactionId = api.createTransaction(
                            userId = userId,
                            concertId = concertId,
                            ticketId = ticket.idTicket,
                            amount = ticket.price,
                            date = Instant.now().toString()
                        )
                        Log.d("ConcertViewModel", "Transaction created: $transactionId")
                    }
                    onComplete(null) // No error
                } catch (e: Exception) {
                    Log.e("ConcertViewModel", "Error creating transaction", e)
                    onComplete(e.message) // Pass error message
                }
            }
        }



    }



