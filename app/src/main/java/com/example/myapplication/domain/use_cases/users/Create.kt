package  com.example.myapplication.domain.use_cases.users

import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.repository.UsersRepository
import javax.inject.Inject

class Create @Inject constructor(private val repository: UsersRepository) {

    suspend operator fun invoke(user: User) = repository.create(user)

}