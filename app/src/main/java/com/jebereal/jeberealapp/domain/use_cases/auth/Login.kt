package com.jebereal.jeberealapp.domain.use_cases.auth


import com.jebereal.jeberealapp.domain.repository.AuthRepository
import javax.inject.Inject

class Login @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String) = repository.login(email, password)

}