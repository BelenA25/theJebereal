package com.example.myapplication.domain.use_cases.auth

import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.model.User

import javax.inject.Inject

class Signup @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(user: User) = repository.signUp(user)

}