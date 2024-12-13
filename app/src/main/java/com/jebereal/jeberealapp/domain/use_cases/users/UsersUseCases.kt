package  com.jebereal.jeberealapp.domain.use_cases.users

data class UsersUseCases(
    val create: Create,
    val getUserById: GetUserById,
    val update: Update,
    val saveImage: SaveImage
)
