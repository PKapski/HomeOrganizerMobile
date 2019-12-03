package pl.polsl.homeorganizer.notes

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.content_notes.*
import pl.polsl.homeorganizer.authentication.AuthenticationManager
import pl.polsl.homeorganizer.R

class NotesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        setSupportActionBar(toolbar)

        val cred =
            AuthenticationManager.getAccessToken(this.applicationContext)
        usernameNotes.text = cred.userId
        tokenNotes.text = cred.token

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

}
