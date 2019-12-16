package pl.polsl.homeorganizer.notes

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson
import pl.polsl.homeorganizer.MainApplication
import pl.polsl.homeorganizer.MySingleton
import pl.polsl.homeorganizer.R
import pl.polsl.homeorganizer.authentication.AuthenticationManager
import pl.polsl.homeorganizer.http.requests.CustomJsonRequest

object NotesContent {
    interface NotesCallback {
        fun onSuccessResponse(result: MutableList<Note>)
    }
    var items: MutableList<Note> = ArrayList()

    fun getChecklists(callback: NotesCallback,context: Context) {
        val credentials = AuthenticationManager.getCredentials(MainApplication.applicationContext())
        val url =
            MainApplication.applicationContext().getString(R.string.server_ip) + "notes?username" + credentials.username + "&householdId=" + credentials.householdId

        val request = CustomJsonRequest(
            Request.Method.GET, url, null,
            Response.Listener { arrayResponse ->
                items = Gson().fromJson(
                    arrayResponse.get("array").toString(),
                    Array<Note>::class.java
                ).toList().toMutableList()
                callback.onSuccessResponse(items)
            },
            Response.ErrorListener {
                if (it.networkResponse.statusCode==403){
                    AuthenticationManager.logout(context)
                }

            }, MainApplication.applicationContext()
        )

        MySingleton.getInstance(MainApplication.applicationContext()).addToRequestQueue(request)
    }
}