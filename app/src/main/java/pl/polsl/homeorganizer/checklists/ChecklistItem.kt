package pl.polsl.homeorganizer.checklists

import java.io.Serializable

data class ChecklistItem(
    var message: String,
    var isChecked: Boolean
): Serializable