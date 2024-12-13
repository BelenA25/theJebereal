package  com.jebereal.jeberealapp.domain.use_cases.users

import com.jebereal.jeberealapp.domain.repository.UsersRepository
import java.io.File
import javax.inject.Inject

class SaveImage @Inject constructor(private val repository: UsersRepository) {

    suspend operator fun invoke(file: File) = repository.saveImage(file)

}