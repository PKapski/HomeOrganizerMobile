package pl.polsl.homeorganizer.checklists

import java.io.Serializable

data class Checklist(
    var id: String?,
    var title: String,
    var creator: String,
    var householdId: String,
    var itemList: MutableList<ChecklistItem>
): Serializable