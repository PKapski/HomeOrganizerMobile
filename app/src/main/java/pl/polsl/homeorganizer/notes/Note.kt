package pl.polsl.homeorganizer.notes

data class Note(
    var id: String,
    var title: String,
    var recipent: String,
    var creator: String,
    var householdId: String,
    var visibleToEveryone: Boolean,
    var message: String
)