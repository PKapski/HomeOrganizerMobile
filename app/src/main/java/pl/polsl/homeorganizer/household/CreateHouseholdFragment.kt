package pl.polsl.homeorganizer.household

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import kotlinx.android.synthetic.main.fragment_create_household.*
import kotlinx.android.synthetic.main.fragment_create_household.view.*
import org.json.JSONObject
import pl.polsl.homeorganizer.MySingleton
import pl.polsl.homeorganizer.R
import pl.polsl.homeorganizer.authentication.AuthenticationManager
import pl.polsl.homeorganizer.notes.NotesActivity
import pl.polsl.homeorganizer.http.requests.CustomStringRequest

class CreateHouseholdFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_household, container, false)

        view.createHouseholdButton.setOnClickListener{
            if (checkIfFieldsAreInvalid()) return@setOnClickListener
            val url = getString(R.string.server_ip)+"households"
            val jsonObject = JSONObject()
            jsonObject.put("name",householdNameEdit.text.toString())
            jsonObject.put("description",householdDescEdit.text.toString())

            val request =
                CustomStringRequest(Request.Method.POST,
                    url,
                    jsonObject,
                    Response.Listener { response ->
                        joinCreatedHousehold(response)
                    },
                    Response.ErrorListener { error ->
                        Log.d("error", error.toString())
                    },
                    this.requireContext().applicationContext
                )
            MySingleton.getInstance(this.requireContext().applicationContext).addToRequestQueue(request)
        }

        return view
    }

    private fun joinCreatedHousehold(householdId: String?) {
        var credentials = AuthenticationManager.getCredentials(this.requireContext().applicationContext)
        credentials.householdId=householdId
        AuthenticationManager.setCredentials(credentials,this.requireContext().applicationContext)
        val url = getString(R.string.server_ip)+"users/"+credentials.username+"/sethousehold?householdId="+credentials.householdId
        val request = CustomStringRequest(
            Request.Method.PATCH,
            url,
            null,
            Response.Listener {
                val intent =
                    Intent(this.requireContext().applicationContext, NotesActivity::class.java)
                startActivity(intent)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()

            },
            this.requireContext().applicationContext
        )

        MySingleton.getInstance(this.requireContext().applicationContext).addToRequestQueue(request)

    }

    private fun checkIfFieldsAreInvalid(): Boolean {
        if (TextUtils.isEmpty(householdNameEdit.text.toString().trim())) {
            householdNameEdit.error=getString(R.string.empty_field_error)
            return true
        }
        if (TextUtils.isEmpty(householdDescEdit.text.toString().trim())) {
            householdDescEdit.error=getString(R.string.empty_field_error)
            return true
        }
        return false
    }

}