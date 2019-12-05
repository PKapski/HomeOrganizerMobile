package pl.polsl.homeorganizer.household

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_household.*
import kotlinx.android.synthetic.main.fragment_join_household.*
import org.json.JSONArray
import pl.polsl.homeorganizer.MySingleton
import pl.polsl.homeorganizer.R
import pl.polsl.homeorganizer.authentication.AuthenticationManager
import pl.polsl.homeorganizer.http.requests.CustomArrayRequest
import pl.polsl.homeorganizer.http.requests.CustomJsonRequest
import pl.polsl.homeorganizer.http.requests.CustomStringRequest
import pl.polsl.homeorganizer.notes.NotesActivity

class MyHousehold : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_household)
        copyIdImage.setOnClickListener {
            copyId()
            createCopiedToast()
        }
        leaveHouseholdIcon.setOnClickListener{
            showConfirmationDialog()
        }
        var household: Household
        val url =
            getString(R.string.server_ip) + "households/" + AuthenticationManager.getCredentials(
                this.applicationContext
            ).householdId
        val request = CustomJsonRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                household = Gson().fromJson(response.toString(), Household::class.java)
                setViewTexts(household)
                getUsersAndCreateTable(household.id)
            },
            Response.ErrorListener {

            },
            this.applicationContext
        )
        MySingleton.getInstance(this.applicationContext).addToRequestQueue(request)
    }

    private fun showConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Leave confirmation")
        dialogBuilder.setMessage("Are you sure you wanna leave Your household?")
        dialogBuilder.setPositiveButton("Yes"){
            dialog, which ->  leaveHousehold()
        }
        dialogBuilder.setNeutralButton("Cancel"){_,_->}
        dialogBuilder.create().show()
    }

    private fun leaveHousehold() {
        val credentials = AuthenticationManager.getCredentials(this.applicationContext)
        credentials.householdId=null
        AuthenticationManager.setCredentials(credentials,this.applicationContext)
        val url = getString(R.string.server_ip)+"users/"+credentials.username+"/sethousehold"

        val request =
            CustomStringRequest(
                Request.Method.PATCH, url, null,
                Response.Listener {
                    val intent = Intent(
                        this.applicationContext,
                        HouseholdManager::class.java
                    )
                    startActivity(intent)
                },
                Response.ErrorListener {
                    householdIdEdit.error = getString(R.string.household_join_error)

                }, this.applicationContext
            )

        MySingleton.getInstance(this.applicationContext).addToRequestQueue(request)
    }

    private fun getUsersAndCreateTable(id: String) {
        val url = getString(R.string.server_ip) + "users/household/" + id
        var users: List<User>
        val request = CustomJsonRequest(Request.Method.GET,url,null,
            Response.Listener {
                    arrayResponse ->
                Log.d("Before gson", arrayResponse.toString())
                users = Gson().fromJson(arrayResponse.get("array").toString(), Array<User>::class.java).toList()
                Log.d("users",users.toString())
                createTable(users)
            },
            Response.ErrorListener {

            }, this.applicationContext)

        MySingleton.getInstance(this.applicationContext).addToRequestQueue(request)

    }

    private fun createTable(users: List<User>) {
        var i=1
        for (user: User in users){
            val row = TableRow(this)
            row.background=resources.getDrawable(R.drawable.text_border,null)
            row.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            val userCount = TextView(this)
            userCount.text = i.toString()
            setStyle(userCount)
            val usernameField = TextView(this)
            usernameField.text = user.username
            setStyle(usernameField)
            val firstNameField = TextView(this)
            firstNameField.text = user.firstName
            setStyle(firstNameField)
            val lastNameField= TextView(this)
            lastNameField.text = user.lastName
            setStyle(lastNameField)
            row.addView(userCount)
            row.addView(usernameField)
            row.addView(firstNameField)
            row.addView(lastNameField)
            usersTable.addView(row)
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
            applicationContext,
            "Id has been copied!",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun copyId() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clipData = ClipData.newPlainText(
            "text",
            AuthenticationManager.getCredentials(this.applicationContext).householdId
        )
        clipboard?.primaryClip = clipData
    }

    private fun setViewTexts(household: Household) {
        householdTitle.text = household.name
        householdIdText.text = household.id
        householdDescriptionText.text = household.description
    }

}

