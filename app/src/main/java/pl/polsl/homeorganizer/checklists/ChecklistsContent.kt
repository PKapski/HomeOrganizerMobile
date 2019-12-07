package pl.polsl.homeorganizer.checklists

import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson
import pl.polsl.homeorganizer.MainApplication
import pl.polsl.homeorganizer.MySingleton
import pl.polsl.homeorganizer.R
import pl.polsl.homeorganizer.authentication.AuthenticationManager
import pl.polsl.homeorganizer.http.requests.CustomJsonRequest


object ChecklistsContent {

    interface ChecklistsCallback {
        fun onSuccessResponse(result: MutableList<Checklist>)
    }

    var items: MutableList<Checklist> = ArrayList()

    fun getChecklists(callback: ChecklistsCallback) {
        val credentials = AuthenticationManager.getCredentials(MainApplication.applicationContext())
        val url =
            MainApplication.applicationContext().getString(R.string.server_ip) + "checklists?username" + credentials.username + "&householdId=" + credentials.householdId

        val request = CustomJsonRequest(
            Request.Method.GET, url, null,
            Response.Listener { arrayResponse ->
                items = Gson().fromJson(
                    arrayResponse.get("array").toString(),
                    Array<Checklist>::class.java
                ).toList().toMutableList()
                callback.onSuccessResponse(items)
            },
            Response.ErrorListener {

            }, MainApplication.applicationContext()
        )

        MySingleton.getInstance(MainApplication.applicationContext()).addToRequestQueue(request)
    }
}