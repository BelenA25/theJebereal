package com.jebereal.jeberealapp.core

data class TicketConcert(
    val idTicket: String = "",
    val price: Double = 0.0,
    var available: Boolean = true,
    val row: Int = 0,
    val seatNumber: Int = 0,
    val type: String = "",
    val concertId: String = ""
)