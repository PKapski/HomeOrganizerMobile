package pl.polsl.homeorganizer

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_user_application.*
import kotlinx.android.synthetic.main.toolbar_with_add_new.*
import pl.polsl.homeorganizer.authentication.AuthenticationManager
import pl.polsl.homeorganizer.authentication.LoginActivity
import pl.polsl.homeorganizer.checklists.Checklist
import pl.polsl.homeorganizer.checklists.ChecklistFragment
import pl.polsl.homeorganizer.checklists.ChecklistInspectActivity
import pl.polsl.homeorganizer.checklists.ChecklistItem
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
        val credentials = AuthenticationManager.getCredentials(this)
        when(nav_view.selectedItemId) {
            R.id.navigation_notes->{
                val note = Note(null,"",credentials.username, credentials.householdId!!,"")
                inspectNote(note)
            }
            R.id.navigation_checklists->{
                val checklist = Checklist(null,"",credentials.username,credentials.householdId!!,
                    mutableListOf())
                inspectChecklist(checklist)
            }
        }

    }

    private fun setToolbar(): BottomNavigationView {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_with_add)
        setSupportActionBar(toolbar)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        return navView
    }

    override fun onListFragmentInteraction(item: Checklist?) {
        inspectChecklist(item)
    }

    override fun onListFragmentInteraction(item: Note?) {
        inspectNote(item)
    }

    private fun inspectNote(item: Note?) {
        val intent = Intent(this, NoteInspectActivity::class.java)
        intent.putExtra("note", item as Serializable)
        startActivity(intent)
    }

    private fun inspectChecklist(item: Checklist?){
        val intent = Intent(this, ChecklistInspectActivity::class.java)
        intent.putExtra("checklist", item as Serializable)
        startActivity(intent)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.account_menu,menu)
            return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_logout -> {
                AuthenticationManager.logout(this)
                this.finish()
            }
        }
        return true
    }

}
