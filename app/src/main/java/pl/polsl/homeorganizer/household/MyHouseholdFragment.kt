package pl.polsl.homeorganizer.household

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_join_household.*
import kotlinx.android.synthetic.main.fragment_my_household.view.*
import kotlinx.android.synthetic.main.toolbar_with_exit.*
import pl.polsl.homeorganizer.MySingleton
import pl.polsl.homeorganizer.R
import pl.polsl.homeorganizer.authentication.AuthenticationManager
import pl.polsl.homeorganizer.http.requests.CustomJsonRequest
import pl.polsl.homeorganizer.http.requests.CustomStringRequest

class MyHouseholdFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_household, container, false)
        view.copyIdImage.setOnClickListener {
            copyId()
            createCopiedToast()
        }
        setHouseholdToolbar()
        (activity as AppCompatActivity).leaveHouseholdIcon.setOnClickListener{
            showConfirmationDialog()
        }
        val url =
            getString(R.string.server_ip) + "households/" + AuthenticationManager.getCredentials(
                this.requireContext().applicationContext
            ).householdId
        val request = CustomJsonRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                val household = Gson().fromJson(response.toString(), Household::class.java)
                setViewTexts(household,view)
                getUsersAndCreateTable(household.id,view)
            },
            Response.ErrorListener {

            },
            this.requireContext().applicationContext
        )
        MySingleton.getInstance(this.requireContext().applicationContext).addToRequestQueue(request)
        return view
    }

    private fun setHouseholdToolbar() {
        val addToolbar =
            (activity as AppCompatActivity).findViewById<Toolbar>(R.id.toolbar_with_add)
        val householdToolbar =
            (activity as AppCompatActivity).findViewById<Toolbar>(R.id.toolbar_basic)
        addToolbar.visibility = GONE
        householdToolbar.visibility = VISIBLE
        householdToolbar.title = "Household"
    }

    private fun showConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(this.requireContext())
        dialogBuilder.setTitle("Leave confirmation")
        dialogBuilder.setMessage("Are you sure you wanna leave Your household?")
        dialogBuilder.setPositiveButton("Yes"){
            dialog, which ->  leaveHousehold()
        }
        dialogBuilder.setNeutralButton("Cancel"){_,_->}
        dialogBuilder.create().show()
    }

    private fun leaveHousehold() {
        val credentials = AuthenticationManager.getCredentials(this.requireContext().applicationContext)
        credentials.householdId=null
        AuthenticationManager.setCredentials(credentials,this.requireContext().applicationContext)
        val url = getString(R.string.server_ip)+"users/"+credentials.username+"/sethousehold"

        val request =
            CustomStringRequest(
                Request.Method.PATCH, url, null,
                Response.Listener {
                    val intent = Intent(
                        this.requireContext().applicationContext,
                        HouseholdManager::class.java
                    )
                    startActivity(intent)
                },
                Response.ErrorListener {
                    householdIdEdit.error = getString(R.string.household_join_error)

                }, this.requireContext().applicationContext
            )

        MySingleton.getInstance(this.requireContext().applicationContext).addToRequestQueue(request)
    }

    private fun getUsersAndCreateTable(id: String, view: View) {
        val url = getString(R.string.server_ip) + "users/household/" + id
        var users: List<User>
        val request = CustomJsonRequest(Request.Method.GET,url,null,
            Response.Listener {
                    arrayResponse ->
                Log.d("Before gson", arrayResponse.toString())
                users = Gson().fromJson(arrayResponse.get("array").toString(), Array<User>::class.java).toList()
                Log.d("users",users.toString())
                createTable(users,view)
            },
            Response.ErrorListener {

            }, this.requireContext().applicationContext)

        MySingleton.getInstance(this.requireContext().applicationContext).addToRequestQueue(request)

    }

    private fun createTable(
        users: List<User>,
        view: View
    ) {
        var i=1
        for (user: User in users){
            val row = TableRow(this.requireContext())
            row.background=resources.getDrawable(R.drawable.text_border,null)
            row.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            val userCount = TextView(this.requireContext())
            userCount.text = i.toString()
            setStyle(userCount)
            val usernameField = TextView(this.requireContext())
            usernameField.text = user.username
            setStyle(usernameField)
            val firstNameField = TextView(this.requireContext())
            firstNameField.text = user.firstName
            setStyle(firstNameField)
            val lastNameField= TextView(this.requireContext())
            lastNameField.text = user.lastName
            setStyle(lastNameField)
            row.addView(userCount)
            row.addView(usernameField)
            row.addView(firstNameField)
            row.addView(lastNameField)
            view.usersTable.addView(row)
            i++

        }
    }

    private fun setStyle(view: TextView){
        view.textAlignment=TEXT_ALIGNMENT_CENTER
        view.background=resources.getDrawable(R.drawable.text_border,null)
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
        view.setPadding(10,0,10,0)
    }
    private fun createCopiedToast() {
        Toast.makeText(
            this.requireContext(),
            "Id has been copied!",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun copyId() {
        val clipboard = this.requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clipData = ClipData.newPlainText(
            "text",
            AuthenticationManager.getCredentials(this.requireContext().applicationContext).householdId
        )
        clipboard?.primaryClip = clipData
    }

    private fun setViewTexts(
        household: Household,
        view: View
    ) {
        view.householdTitle.text = household.name
        view.householdIdText.text = household.id
        view.householdDescriptionText.text = household.description
    }

}

