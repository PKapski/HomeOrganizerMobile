package pl.polsl.homeorganizer.household

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import kotlinx.android.synthetic.main.fragment_create_household.*
import kotlinx.android.synthetic.main.fragment_join_household.*
import kotlinx.android.synthetic.main.fragment_join_household.view.*
import pl.polsl.homeorganizer.MySingleton
import pl.polsl.homeorganizer.R
import pl.polsl.homeorganizer.UserApplication
import pl.polsl.homeorganizer.authentication.AuthenticationManager
import pl.polsl.homeorganizer.http.requests.CustomStringRequest
import pl.polsl.homeorganizer.notes.NoteFragment

class JoinHouseholdFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_join_household, container, false)

        view.joinHouseholdButton.setOnClickListener{
            val id = householdIdEdit.text.toString()
            if (TextUtils.isEmpty(id.trim())) {
                householdNameEdit.error = getString(R.string.empty_field_error)
                return@setOnClickListener
            }
            val credentials = AuthenticationManager.getCredentials(this.requireContext().applicationContext)
            val url = getString(R.string.server_ip)+"users/"+credentials.username+"/sethousehold?householdId="+id

            val request =
                CustomStringRequest(
                    Request.Method.PATCH, url, null,
                    Response.Listener {
                        val intent = Intent(
                            this.requireContext().applicationContext,
                            UserApplication::class.java
                        )
                        startActivity(intent)
                        credentials.householdId = id
                        AuthenticationManager.setCredentials(
                            credentials,
                            this.requireContext().applicationContext
                        )
                    },
                    Response.ErrorListener {
                        householdIdEdit.error = getString(R.string.household_join_error)

                    }, this.requireContext().applicationContext
                )

            MySingleton.getInstance(this.requireContext().applicationContext).addToRequestQueue(request)
        }
        return view
    }

}