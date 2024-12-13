package com.jebereal.jeberealapp.domain.use_cases.auth

import com.jebereal.jeberealapp.domain.repository.AuthRepository
import com.jebereal.jeberealapp.domain.model.User

import javax.inject.Inject

class Signup @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(user: User) = repository.signUp(user)

}