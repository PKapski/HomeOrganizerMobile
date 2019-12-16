package pl.polsl.homeorganizer.authentication

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.Gson

object AuthenticationManager {

    private const val PREFERENCES_NAME="credentials"
    private const val USER_CREDENTIALS = "user_credentials"

    fun setCredentials(credentials: Credentials, context: Context) {
        val sp = context.getSharedPreferences(
            PREFERENCES_NAME, Context.MODE_PRIVATE)

        val jsonObject = Gson().toJson(credentials)
        sp!!.edit().putString(USER_CREDENTIALS, jsonObject)
            .apply()
    }

    fun getCredentials(context: Context): Credentials {
        val sp = context.getSharedPreferences(
            PREFERENCES_NAME, Context.MODE_PRIVATE)
        val json = sp!!.getString(USER_CREDENTIALS, "")
        if (json==""){
            return Credentials("","","")
        }
        return Gson().fromJson(json, Credentials::class.java)
    }

    private fun clearCredentials(context: Context){
        val sp = context.getSharedPreferences(
            PREFERENCES_NAME, Context.MODE_PRIVATE)
        sp.edit().clear().apply()
    }
    fun logout(context: Context) {
        clearCredentials(context)
        val intent = Intent(context,LoginActivity::class.java)
        context.startActivity(intent)
    }
}