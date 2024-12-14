package com.jebereal.jeberealapp.core

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
