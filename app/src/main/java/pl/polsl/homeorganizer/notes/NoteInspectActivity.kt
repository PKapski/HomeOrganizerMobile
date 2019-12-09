package pl.polsl.homeorganizer.notes

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_note_inspect.*
import org.json.JSONObject
import pl.polsl.homeorganizer.MySingleton
import pl.polsl.homeorganizer.R
import pl.polsl.homeorganizer.UserApplication
import pl.polsl.homeorganizer.http.requests.CustomJsonRequest
import pl.polsl.homeorganizer.http.requests.CustomStringRequest
import java.util.*

class NoteInspectActivity : AppCompatActivity() {

    var editMode: Boolean = false
    lateinit var note: Note

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_inspect)
        setSupportActionBar(findViewById(R.id.toolbar_inspection_note))
        note = intent.extras?.get("note") as Note
        creatorTextNote.text = note.creator
        titleTextNote.setText(note.title)
        messageTextNote.setText(note.message)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun changeEditMode(item: MenuItem) {
        editMode = !editMode
        if (editMode) {
            item.setIcon(R.drawable.ic_apply)
            titleTextNote.isClickable = true
            titleTextNote.isFocusableInTouchMode = true
            titleTextNote.setBackgroundResource(R.drawable.text_edit)
            titleTextNote.requestFocus()
            messageTextNote.isClickable = true
            messageTextNote.isFocusableInTouchMode = true
            messageTextNote.setBackgroundResource(R.drawable.text_edit)


        } else {
            item.setIcon(R.drawable.ic_edit)
            titleTextNote.isFocusableInTouchMode = false
            titleTextNote.isClickable = false
            titleTextNote.setBackgroundColor(resources.getColor(R.color.colorBackground, null))
            messageTextNote.isFocusableInTouchMode = false
            messageTextNote.isClickable = false
            messageTextNote.setBackgroundResource(R.drawable.text_border)
            currentFocus?.clearFocus()
            saveChangesInDatabase()
        }

    }

    private fun saveChangesInDatabase() {
        note.title = titleTextNote.text.toString()
        note.message = messageTextNote.text.toString()
        val url = getString(R.string.server_ip) + "notes"
        val json = JSONObject(Gson().toJson(note))
        val request = CustomStringRequest(Request.Method.POST, url, json,
            Response.Listener {
                Toast.makeText(
                    applicationContext, "Edit successfull!",
                    Toast.LENGTH_LONG
                ).show()
            },
            Response.ErrorListener {
                Toast.makeText(
                    applicationContext, "Error editing note!",
                    Toast.LENGTH_LONG
                ).show()
            }, this.applicationContext
        )
        MySingleton.getInstance(this.applicationContext).addToRequestQueue(request)

    }

    private fun showDeleteConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Delete confirmation")
        dialogBuilder.setMessage("Are you sure you wanna delete this note?")
        dialogBuilder.setPositiveButton("Yes") { dialog, which ->
            deleteNote()
        }
        dialogBuilder.setNeutralButton("Cancel") { _, _ -> }
        dialogBuilder.create().show()
    }

    private fun deleteNote() {
        val url = getString(R.string.server_ip) + "notes/" + note.id
        val request = CustomJsonRequest(Request.Method.DELETE, url, null,
            Response.Listener {
                val intent = Intent(this, UserApplication::class.java)
                intent.putExtra("fragmentToLoad", R.id.navigation_notes)
                startActivity(intent)
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

    private fun getDateFromId(id: String): CharSequence? {
        val date = Date(id.substring(0, 8).toInt(16) * 1000L)
        return formatDate(date)
    }

    private fun formatDate(date: Date): String? {
        val sdf = SimpleDateFormat("yyyy-dd-MM HH:mm:ss")
        return sdf.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.inspection_menu, menu)
        if (note.id == null) {
            changeEditMode(menu!!.findItem(R.id.action_edit))
            dateTextNote.setText(formatDate(Date()))
        } else {
            dateTextNote.setText(getDateFromId(note.id!!))

        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_edit -> changeEditMode(item)
            R.id.action_delete -> showDeleteConfirmationDialog()
        }
        return true
    }

}
