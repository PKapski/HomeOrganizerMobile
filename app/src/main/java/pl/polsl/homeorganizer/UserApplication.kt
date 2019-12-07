package pl.polsl.homeorganizer

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import pl.polsl.homeorganizer.checklists.Checklist
import pl.polsl.homeorganizer.checklists.ChecklistFragment
import pl.polsl.homeorganizer.notes.Note
import pl.polsl.homeorganizer.notes.NoteFragment

class UserApplication : AppCompatActivity(), ChecklistFragment.OnListFragmentInteractionListener, NoteFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_application)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
               R.id.navigation_notes,R.id.navigation_checklists,R.id.navigation_checklists,R.id.navigation_household
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onListFragmentInteraction(item: Checklist?) {
        println(item)
    }

    override fun onListFragmentInteraction(item: Note?) {
        println(item)
    }
}
