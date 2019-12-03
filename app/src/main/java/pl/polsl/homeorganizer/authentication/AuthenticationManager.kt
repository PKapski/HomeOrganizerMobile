package pl.polsl.homeorganizer.authentication

import android.content.Context
import com.google.gson.Gson

object AuthenticationManager {

    private const val PREFERENCES_NAME="credentials"
    private const val USER_CREDENTIALS = "user_credentials"

    fun saveCredentials(context: Context, credentials: Credentials) {
        val sp = context.getSharedPreferences(
            PREFERENCES_NAME, Context.MODE_PRIVATE)

        val jsonObject = Gson().toJson(credentials)
        sp!!.edit().putString(USER_CREDENTIALS, jsonObject)
            .apply()
    }

    fun getAccessToken(context: Context): Credentials {
        val sp = context.getSharedPreferences(
            PREFERENCES_NAME, Context.MODE_PRIVATE)
        val json = sp!!.getString(USER_CREDENTIALS, "")
        return Gson().fromJson(json, Credentials::class.java)
    }
}