package pl.polsl.homeorganizer.authentication

data class Credentials(
    val userId: String,
    val token: String,
    val groupId: String?
)

