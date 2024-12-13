package com.jebereal.jeberealapp.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.jebereal.jeberealapp.domain.model.Response
import com.jebereal.jeberealapp.domain.model.User

interface AuthRepository {

    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Response<FirebaseUser>
    suspend fun signUp(user: User): Response<FirebaseUser>
    fun logout()
}