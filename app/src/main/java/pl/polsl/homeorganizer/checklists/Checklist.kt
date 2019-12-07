package pl.polsl.homeorganizer.checklists

data class Checklist(
    var id: String,
    var title: String,
    var recipent: String,
    var creator: String,
    var householdId: String,
    var visibleToEveryone: Boolean,
    var itemList: List<ChecklistItem>
)