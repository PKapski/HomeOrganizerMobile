package pl.polsl.homeorganizer.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_note_inspect.*
import pl.polsl.homeorganizer.R

class NoteInspectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_inspect)
        val note = intent.extras?.get("note") as Note
        creatorTextNote.text = note.creator
        recipentTextNote.text = note.recipent
        titleTextNote.setText(note.title)
        messageTextNote.setText(note.message)
    }
}
