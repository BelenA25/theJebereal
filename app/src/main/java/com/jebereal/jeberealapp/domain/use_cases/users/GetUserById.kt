package  com.jebereal.jeberealapp.domain.use_cases.users

import com.jebereal.jeberealapp.domain.repository.UsersRepository
import javax.inject.Inject

class GetUserById @Inject constructor(private val repository: UsersRepository) {

    operator fun invoke(id: String) = repository.getUserById(id)

}