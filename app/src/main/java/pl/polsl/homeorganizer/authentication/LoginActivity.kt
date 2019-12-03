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
import pl.polsl.homeorganizer.household.JoinHousehold
import pl.polsl.homeorganizer.register.CustomRequest
import pl.polsl.homeorganizer.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener{
            if (usernameLoginText.text.toString().length<5){
                usernameLoginText.error=getString(R.string.invalid_username)
                return@setOnClickListener
            }

            var username = usernameLoginText.text.toString()
            var password = passwordLoginText.text.toString()
            val url = "http://192.168.0.248:8080/users/auth"
            val jsonObject = JSONObject()

            jsonObject.put("username", username)
            jsonObject.put("password", password)
            val request = CustomRequest(Request.Method.POST, url, jsonObject,
                Response.Listener { response ->
                    val headers: JSONObject = response.getJSONObject("headers")
                    val credentials =
                        Credentials(
                            headers.get("Authorization").toString(),
                            headers.get("Username").toString(),
                            getHousehold(headers)
                        )
                    AuthenticationManager.saveCredentials(this.applicationContext,credentials)
                    updateUiWithUser(username)
                }, Response.ErrorListener {
                    showLoginFailed()
                })
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
        val intent = Intent(this, JoinHousehold::class.java)
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



