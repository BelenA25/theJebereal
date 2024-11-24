package  com.example.myapplication.domain.use_cases.users

import com.example.myapplication.domain.repository.UsersRepository
import java.io.File
import javax.inject.Inject

class SaveImage @Inject constructor(private val repository: UsersRepository) {

    suspend operator fun invoke(file: File) = repository.saveImage(file)

}