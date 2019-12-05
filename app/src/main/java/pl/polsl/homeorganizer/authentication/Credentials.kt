package pl.polsl.homeorganizer.authentication

data class Credentials(
    var username: String,
    var token: String,
    var householdId: String?
)

