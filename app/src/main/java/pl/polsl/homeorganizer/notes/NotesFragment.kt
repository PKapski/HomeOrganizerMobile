package pl.polsl.homeorganizer.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_notes.view.*
import kotlinx.android.synthetic.main.fragment_notes.view.*
import pl.polsl.homeorganizer.R
import pl.polsl.homeorganizer.authentication.AuthenticationManager

class NotesFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notes, container, false)
        val cred =
            AuthenticationManager.getCredentials(this.requireContext().applicationContext)
        view.usernameNotes.text = cred.username
        view.tokenNotes.text = cred.token

        view.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        return view
    }

}
