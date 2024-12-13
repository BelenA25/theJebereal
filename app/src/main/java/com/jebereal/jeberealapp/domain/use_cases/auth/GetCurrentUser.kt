package com.jebereal.jeberealapp.domain.use_cases.auth


import com.jebereal.jeberealapp.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUser @Inject constructor(private val repository: AuthRepository) {

    operator fun invoke() = repository.currentUser

}