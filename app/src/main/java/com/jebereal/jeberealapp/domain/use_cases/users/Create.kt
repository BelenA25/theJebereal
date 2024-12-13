package  com.jebereal.jeberealapp.domain.use_cases.users

import com.jebereal.jeberealapp.domain.model.User
import com.jebereal.jeberealapp.domain.repository.UsersRepository
import javax.inject.Inject

class Create @Inject constructor(private val repository: UsersRepository) {

    suspend operator fun invoke(user: User) = repository.create(user)

}