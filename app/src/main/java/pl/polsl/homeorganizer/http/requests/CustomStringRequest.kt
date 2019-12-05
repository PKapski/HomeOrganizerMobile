package pl.polsl.homeorganizer.http.requests

import android.content.Context
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import pl.polsl.homeorganizer.authentication.AuthenticationManager
import java.io.UnsupportedEncodingException

open class CustomStringRequest (
    method: Int,
    url: String,
    private var jsonRequest: JSONObject?,
    listener: Response.Listener<String>,
    errorListener: Response.ErrorListener,
    val context: Context
) : StringRequest(method,url,listener,errorListener) {


    override fun getBodyContentType(): String {
        return "application/json; charset=utf-8"
    }

    override fun getHeaders(): MutableMap<String, String> {
        val headers = HashMap<String,String>()
        headers["Authorization"] = AuthenticationManager?.getCredentials(context).token
        return headers
    }

    override fun getBody(): ByteArray? {
        try{
            return if (jsonRequest==null) null else jsonRequest.toString().toByteArray()
        }catch(e: UnsupportedEncodingException) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonRequest.toString(), "utf-8");
            return null
        }
    }
}