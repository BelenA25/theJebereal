package com.jebereal.jeberealapp.domain.use_cases.users

import com.jebereal.jeberealapp.domain.model.User
import com.jebereal.jeberealapp.domain.repository.UsersRepository
import javax.inject.Inject

class Update @Inject constructor(private val repository: UsersRepository) {

    suspend operator fun invoke(user: User) = repository.update(user)

}