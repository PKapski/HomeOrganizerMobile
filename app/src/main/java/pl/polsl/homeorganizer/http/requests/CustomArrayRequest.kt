package pl.polsl.homeorganizer.http.requests

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray
import pl.polsl.homeorganizer.authentication.AuthenticationManager

class CustomArrayRequest(
    url: String,
    listener: Response.Listener<JSONArray>,
    errorListener: Response.ErrorListener,
    val context: Context
) : JsonArrayRequest(url,listener,errorListener){

    override fun getHeaders(): MutableMap<String, String> {
        val headers = HashMap<String,String>()
        headers["Authorization"] = AuthenticationManager?.getCredentials(context).token
        return headers
    }
}
