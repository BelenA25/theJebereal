package com.jebereal.jeberealapp.data.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jebereal.jeberealapp.core.Concert
import com.jebereal.jeberealapp.core.TicketConcert
import com.jebereal.jeberealapp.domain.repository.ConcertApi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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
                            task.exception
                                ?: Exception("Unknown error updating ticket availability")
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
                    continuation.resumeWithException(
                        task.exception ?: Exception("Error creating transaction")
                    )
                }
            }
    }
}