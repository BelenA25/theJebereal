package com.example.myapplication.domain.use_cases.auth


import com.example.myapplication.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUser @Inject constructor(private val repository: AuthRepository) {

    operator fun invoke() = repository.currentUser

}