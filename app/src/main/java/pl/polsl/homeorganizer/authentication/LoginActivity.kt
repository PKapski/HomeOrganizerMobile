package pl.polsl.homeorganizer.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import pl.polsl.homeorganizer.*
import pl.polsl.homeorganizer.household.HouseholdManager
import pl.polsl.homeorganizer.household.MyHousehold
import pl.polsl.homeorganizer.http.requests.CustomJsonRequest
import pl.polsl.homeorganizer.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener{
            if (usernameLoginText.text.toString().trim().length<5){
                usernameLoginText.error=getString(R.string.invalid_username)
                return@setOnClickListener
            }

            var username = usernameLoginText.text.toString()
            var password = passwordLoginText.text.toString()
            val url = getString(R.string.server_ip)+"users/auth"
            val jsonObject = JSONObject()

            jsonObject.put("username", username)
            jsonObject.put("password", password)
            val request = CustomJsonRequest(
                Request.Method.POST,
                url,
                jsonObject,
                Response.Listener { response ->
                    val headers: JSONObject = response.getJSONObject("headers")
                    val credentials =
                        Credentials(
                            headers.get("Username").toString(),
                            headers.get("Authorization").toString(),
                            getHousehold(headers)
                        )
                    AuthenticationManager.setCredentials(credentials, this.applicationContext)
                    updateUiWithUser(username)
                },
                Response.ErrorListener {
                    showLoginFailed()
                },
                this.applicationContext
            )
            MySingleton.getInstance(this.applicationContext).addToRequestQueue(request)
        }

        registerNowText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getHousehold(headers: JSONObject): String? {
        return if (headers.has("Household")){
            headers.get("Household").toString()
        }else{
            null
        }
    }

    private fun updateUiWithUser(username: String) {
        val welcome = getString(R.string.welcome)
        val intent: Intent = if (AuthenticationManager.getCredentials(this.applicationContext).householdId==null) {
            Intent(this, HouseholdManager::class.java)
        }else{
            Intent(this, MyHousehold::class.java)
        }
        startActivity(intent)
        Toast.makeText(
            applicationContext,
            "$welcome $username!",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed() {
        Toast.makeText(applicationContext, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
    }
}



