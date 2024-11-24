package com.example.myapplication.domain.use_cases.users

import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.repository.UsersRepository
import javax.inject.Inject

class Update @Inject constructor(private val repository: UsersRepository) {

    suspend operator fun invoke(user: User) = repository.update(user)

}