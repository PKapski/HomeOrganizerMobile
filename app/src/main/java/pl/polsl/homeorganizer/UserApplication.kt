package pl.polsl.homeorganizer

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_user_application.*
import kotlinx.android.synthetic.main.toolbar_with_add_new.*
import pl.polsl.homeorganizer.checklists.Checklist
import pl.polsl.homeorganizer.checklists.ChecklistFragment
import pl.polsl.homeorganizer.notes.Note
import pl.polsl.homeorganizer.notes.NoteFragment
import pl.polsl.homeorganizer.notes.NoteInspectActivity
import java.io.Serializable

class UserApplication : AppCompatActivity(), ChecklistFragment.OnListFragmentInteractionListener, NoteFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_application)
        val navView: BottomNavigationView = setToolbar()
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
               R.id.navigation_notes,R.id.navigation_checklists,R.id.navigation_checklists,R.id.navigation_household
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        addNewToolbarButton.setOnClickListener{
            processAddNewClick()
        }
    }

    private fun processAddNewClick() {
        val intent: Intent
        val current_fragment = nav_view.menu.findItem(nav_view.selectedItemId).toString()
        when(current_fragment) {
            //TODO
        }

    }

    private fun setToolbar(): BottomNavigationView {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_with_add)
        setSupportActionBar(toolbar)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        return navView
    }

    override fun onListFragmentInteraction(item: Checklist?) {
        println(item)
    }

    override fun onListFragmentInteraction(item: Note?) {
        val intent = Intent(this, NoteInspectActivity::class.java)
        intent.putExtra("note", item as Serializable)
        startActivity(intent)
    }
}
