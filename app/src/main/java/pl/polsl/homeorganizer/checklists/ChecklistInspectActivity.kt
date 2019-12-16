package pl.polsl.homeorganizer.checklists

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_checklist_inspect.*
import org.json.JSONObject
import pl.polsl.homeorganizer.MySingleton
import pl.polsl.homeorganizer.R
import pl.polsl.homeorganizer.http.requests.CustomJsonRequest
import pl.polsl.homeorganizer.http.requests.CustomStringRequest
import java.util.*

class ChecklistInspectActivity : AppCompatActivity(),
    ChecklistItemListFragment.OnListFragmentInteractionListener {

    var editMode: Boolean = false
    lateinit var checklist: Checklist
    lateinit var currentFragment: Fragment
    var ARRAY_OF_OPTIONS = arrayOf("Edit", "Delete")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checklist_inspect)
        setSupportActionBar(findViewById(R.id.toolbar_inspection_checklist))
        checklist = intent.extras?.get("checklist") as Checklist

        val tran = supportFragmentManager.beginTransaction()
        val frag = ChecklistItemListFragment.newInstance(checklist)
        currentFragment = frag
        tran.replace(R.id.fragment_container, frag)
        tran.commit()

        creatorTextChecklist.text = checklist.creator
        titleTextChecklist.setText(checklist.title)

        checkIfChecklistHasItems()

        addNewItemButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle("Add new item")
            val dialogLayout = inflater.inflate(R.layout.dialog_with_edittext, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.newItemText)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { _, _ ->
                checklist.itemList.add(ChecklistItem(editText.text.toString(), false))
                saveChangesInDatabase()
                checkIfChecklistHasItems()
            }
            builder.setNeutralButton("Cancel") { _, _ -> }
            builder.show()
        }

    }

    override fun onListFragmentInteraction(item: ChecklistItem?) {
        if (editMode) {
            lateinit var dialog: AlertDialog
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("What would you like to do?")
            dialogBuilder.setSingleChoiceItems(ARRAY_OF_OPTIONS, -1) { _, which ->
                val option = ARRAY_OF_OPTIONS[which]
                if (option == "Edit") {
                    val builder = AlertDialog.Builder(this)
                    val inflater = layoutInflater
                    builder.setTitle("Edit an item")
                    val dialogLayout = inflater.inflate(R.layout.dialog_with_edittext, null)
                    val editText = dialogLayout.findViewById<EditText>(R.id.newItemText)
                    editText.setText(item?.message)
                    builder.setView(dialogLayout)
                    builder.setPositiveButton("OK") { _, _ ->
                        item?.message = editText.text.toString()
                        saveChangesInDatabase()
                    }
                    builder.setNeutralButton("Cancel") { _, _ -> }
                    builder.show()
                } else {
                    checklist.itemList.remove(item)
                    saveChangesInDatabase()
                    checkIfChecklistHasItems()
                }
                dialog.dismiss()
            }
            dialogBuilder.setNeutralButton("Cancel") { _, _ -> }
            dialog = dialogBuilder.create()
            dialog.show()
        } else {
            item?.isChecked = !item?.isChecked!!
            saveChangesInDatabase()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.inspection_menu, menu)
        if (checklist.id == null) {
            changeEditMode(menu!!.findItem(R.id.action_edit))
            dateTextChecklist.setText(formatDate(Date()))
        } else {
            dateTextChecklist.setText(getDateFromId(checklist.id!!))

        }
        return true
    }

    private fun changeEditMode(item: MenuItem) {

        if (!editMode) {
            item.setIcon(R.drawable.ic_apply)
            titleTextChecklist.isClickable = true
            titleTextChecklist.isFocusableInTouchMode = true
            titleTextChecklist.setBackgroundResource(R.drawable.text_edit)
            titleTextChecklist.requestFocus()
        } else {
            item.setIcon(R.drawable.ic_edit)
            titleTextChecklist.isFocusableInTouchMode = false
            titleTextChecklist.isClickable = false
            titleTextChecklist.setBackgroundColor(resources.getColor(R.color.colorBackground, null))
            currentFocus?.clearFocus()
            saveChangesInDatabase()
        }
        editMode = !editMode

    }

    private fun saveChangesInDatabase() {
        checklist.title = titleTextChecklist.text.toString()
        val url = getString(R.string.server_ip) + "checklists"
        val json = JSONObject(Gson().toJson(checklist))
        val request = CustomStringRequest(
            Request.Method.POST, url, json,
            Response.Listener {
                currentFragment.onResume()
            },
            Response.ErrorListener {
                Toast.makeText(
                    applicationContext, "Error editing checklist!",
                    Toast.LENGTH_LONG
                ).show()
            }, this.applicationContext
        )
        MySingleton.getInstance(this.applicationContext).addToRequestQueue(request)

    }

    private fun getDateFromId(id: String): CharSequence? {
        val date = Date(id.substring(0, 8).toInt(16) * 1000L)
        return formatDate(date)
    }

    private fun formatDate(date: Date): String? {
        val sdf = SimpleDateFormat("yyyy-dd-MM HH:mm:ss")
        return sdf.format(date)
    }

    private fun showDeleteConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Delete confirmation")
        dialogBuilder.setMessage("Are you sure you wanna delete this checklist?")
        dialogBuilder.setPositiveButton("Yes") { dialog, which ->
            deleteChecklist()
        }
        dialogBuilder.setNeutralButton("Cancel") { _, _ -> }
        dialogBuilder.create().show()
    }

    private fun deleteChecklist() {
        val url = getString(R.string.server_ip) + "checklists/" + checklist.id
        val request = CustomJsonRequest(
            Request.Method.DELETE, url, null,
            Response.Listener {
                this.finish()
                Toast.makeText(
                    applicationContext, "Note successfully deleted!",
                    Toast.LENGTH_LONG
                ).show()
            },
            Response.ErrorListener {
                Toast.makeText(
                    applicationContext, "Error deleting note!",
                    Toast.LENGTH_LONG
                ).show()
            }, this.applicationContext
        )

        MySingleton.getInstance(this.applicationContext).addToRequestQueue(request)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_edit -> changeEditMode(item)
            R.id.action_delete -> showDeleteConfirmationDialog()
        }
        return true
    }

    private fun checkIfChecklistHasItems() {
        if (checklist.itemList.isEmpty()) {
            noItemsText.visibility = VISIBLE
        } else {
            noItemsText.visibility = GONE
        }
    }
}
