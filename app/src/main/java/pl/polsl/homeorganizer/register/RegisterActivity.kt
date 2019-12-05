package pl.polsl.homeorganizer.register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import pl.polsl.homeorganizer.MySingleton
import pl.polsl.homeorganizer.R
import pl.polsl.homeorganizer.authentication.LoginActivity
import pl.polsl.homeorganizer.http.requests.CustomJsonRequest

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton.setOnClickListener {
            if (checkIfFieldsAreInvalid()) return@setOnClickListener
            val url = getString(R.string.server_ip)+"users"
            val jsonObject = JSONObject()

            jsonObject.put("username",usernameRegisterText.text.toString())
            jsonObject.put("email", emailText.text.toString())
            jsonObject.put("firstName", firstNameText.text.toString())
            jsonObject.put("lastName", lastNameText.text.toString())
            jsonObject.put("password", passwordRegisterText.text.toString())

            val request = CustomJsonRequest(
                Request.Method.POST,
                url,
                jsonObject,
                Response.Listener { response ->
                    Toast.makeText(
                        applicationContext, "User successfully created!",
                        Toast.LENGTH_LONG
                    ).show()
                    intent = Intent(
                        this,
                        LoginActivity::class.java
                    )
                    startActivity(intent)
                },
                Response.ErrorListener {
                    errorText.text = getString(R.string.uniqennes_error)
                },
                this.applicationContext
            )
           // queue.add(request)
            MySingleton.getInstance(this.applicationContext).addToRequestQueue(request)
        }
        loginHereButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkIfFieldsAreInvalid(): Boolean {
        if (TextUtils.isEmpty(usernameRegisterText.text.toString().trim()) || usernameRegisterText.text.toString().trim().length<5) {
            usernameRegisterText.error = getString(R.string.invalid_username)
            return true
        }
        if (TextUtils.isEmpty(emailText.text.toString().trim()) || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailText.text.toString().trim()).matches()) {
            emailText.error = getString(R.string.invalid_email)
            return true
        }
        if (passwordRegisterText.text.toString().trim().length <5){
            passwordRegisterText.error = getString(R.string.invalid_password)
            return true
        }
        if (passwordRegisterText.text.toString().trim() != repeatPasswordText.text.toString().trim()) {
            repeatPasswordText.error = getString(R.string.passwords_error)
            return true
        }
        return false
    }
}
