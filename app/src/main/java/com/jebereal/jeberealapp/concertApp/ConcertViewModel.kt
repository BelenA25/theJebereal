package com.jebereal.jeberealapp.concertApp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// Definición del enum para géneros musicales
enum class MusicGenre {
    ROCK, POP, JAZZ, CLASSICAL, ELECTRONIC, HIP_HOP, REGGAETON, METAL
}

// Data class para conciertos
data class Concert(
    val id: String = "",
    val name: String = "",
    val genre: String = "",
    val price: Double = 0.0,
    val date: String = "",
    val venue: String = "",
    val popularity: Int = 0,
    val discount: Double? = null,
    val imageUrl: String = ""
)

data class TicketConcert(
    val idTicket: String = "",
    val price: Double = 0.0,
    var available: Boolean = true,
    val row: Int = 0,
    val seatNumber: Int = 0,
    val type: String = "",
    val concertId: String = ""
)


// Interface para el API
interface ConcertApi {
    suspend fun getPopularConcerts(): List<Concert>
    suspend fun getBestOffersConcerts(): List<Concert>
    suspend fun getCalendarConcerts(): List<Concert>
    suspend fun getConcertDetails(id: String): Concert
    suspend fun getTicketsForConcert(concertId: String): List<TicketConcert> // Nuevo método
    suspend fun updateTicketAvailability(ticketId: String, isAvailable: Boolean)
    suspend fun createTransaction(
        userId: String,
        concertId: String,
        ticketId: String,
        amount: Double,
        date: String
    ): String

}

class FirebaseConcertApi @Inject constructor(
    private val database: FirebaseDatabase
) : ConcertApi {
    private val concertsRef = database.getReference("concerts")
    private val ticketsRef = database.getReference("tickets")
    private val transactionsRef = database.getReference("transactions")


    override suspend fun getPopularConcerts(): List<Concert> =
        suspendCancellableCoroutine { continuation ->
            concertsRef.orderByChild("popularity")
                .limitToLast(5)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val concerts = snapshot.children.mapNotNull {
                            val concert = it.getValue(Concert::class.java)
                            concert?.copy(id = it.key ?: "")
                        }
                        continuation.resume(concerts.reversed())
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(
                            "FirebaseConcertApi",
                            "Error fetching popular concerts",
                            error.toException()
                        )
                        continuation.resumeWithException(error.toException())
                    }
                })
        }

    override suspend fun getBestOffersConcerts(): List<Concert> =
        suspendCancellableCoroutine { continuation ->
            concertsRef.orderByChild("discount").startAt(1.0)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val concerts = snapshot.children.mapNotNull {
                            val concert = it.getValue(Concert::class.java)
                            concert?.copy(id = it.key ?: "")
                        }
                        continuation.resume(concerts)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(
                            "FirebaseConcertApi",
                            "Error fetching best offers",
                            error.toException()
                        )
                        continuation.resumeWithException(error.toException())
                    }
                })
        }

    override suspend fun getCalendarConcerts(): List<Concert> =
        suspendCancellableCoroutine { continuation ->
            concertsRef.orderByChild("date")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val concerts = snapshot.children.mapNotNull {
                            val concert = it.getValue(Concert::class.java)
                            concert?.copy(id = it.key ?: "")
                        }
                        continuation.resume(concerts)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(
                            "FirebaseConcertApi",
                            "Error fetching calendar concerts",
                            error.toException()
                        )
                        continuation.resumeWithException(error.toException())
                    }
                })
        }

    override suspend fun getConcertDetails(id: String): Concert =
        suspendCancellableCoroutine { continuation ->
            concertsRef.child(id)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val concert = snapshot.getValue(Concert::class.java)
                        if (concert != null) {
                            continuation.resume(concert.copy(id = snapshot.key ?: id))
                        } else {
                            continuation.resumeWithException(Exception("Concert not found"))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(
                            "FirebaseConcertApi",
                            "Error fetching concert details",
                            error.toException()
                        )
                        continuation.resumeWithException(error.toException())
                    }
                })
        }

    override suspend fun getTicketsForConcert(concertId: String): List<TicketConcert> =
        suspendCancellableCoroutine { continuation ->
            ticketsRef
                .orderByChild("concertId").equalTo(concertId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val tickets = snapshot.children.mapNotNull {
                            val ticket = it.getValue(TicketConcert::class.java)
                            ticket?.copy(idTicket = it.key ?: "")
                        }
                        continuation.resume(tickets)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("FirebaseConcertApi", "Error fetching tickets", error.toException())
                        continuation.resumeWithException(error.toException())
                    }
                })
        }

    override suspend fun updateTicketAvailability(ticketId: String, isAvailable: Boolean) =
        suspendCancellableCoroutine { continuation ->
            ticketsRef.child(ticketId).child("available").setValue(isAvailable)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Unknown error updating ticket availability")
                        )
                    }
                }
        }


    override suspend fun createTransaction(
        userId: String,
        concertId: String,
        ticketId: String,
        amount: Double,
        date: String
    ): String = suspendCancellableCoroutine { continuation ->
        val transactionId = transactionsRef.push().key ?: UUID.randomUUID().toString()
        val transaction = mapOf(
            "userId" to userId,
            "concertId" to concertId,
            "ticketId" to ticketId,
            "amount" to amount,
            "date" to date
        )
        transactionsRef.child(transactionId).setValue(transaction)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(transactionId)
                } else {
                    continuation.resumeWithException(task.exception ?: Exception("Error creating transaction"))
                }
            }
    }

// Hilt Module for Dependency Injection

    abstract class ConcertApiModule {
        abstract fun bindConcertApi(
            firebaseConcertApi: FirebaseConcertApi
        ): ConcertApi

        companion object {

            fun provideFirebaseDatabase(): FirebaseDatabase {
                return Firebase.database
            }
        }
    }

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


}
