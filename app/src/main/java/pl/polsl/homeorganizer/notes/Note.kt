package pl.polsl.homeorganizer.notes

import java.io.Serializable

data class Note (
    var id: String?,
    var title: String,
    var creator: String,
    var householdId: String,
    var message: String
): Serializable