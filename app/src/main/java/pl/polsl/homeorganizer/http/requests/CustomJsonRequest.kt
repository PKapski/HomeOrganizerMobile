package pl.polsl.homeorganizer.http.requests

import android.content.Context
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import org.json.JSONObject
import pl.polsl.homeorganizer.authentication.AuthenticationManager
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

open class CustomJsonRequest(
    method: Int,
    url: String,
    jsonRequest: JSONObject?,
    listener: Response.Listener<JSONObject>,
    errorListener: Response.ErrorListener,
    val context: Context
) : JsonObjectRequest(method,url,jsonRequest,listener,errorListener){

    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
        return try {
            val jsonString = String(
                response?.data ?: ByteArray(0),
                Charset.forName(HttpHeaderParser.parseCharset(response?.headers)))
            val result: JSONObject?
            if (jsonString.isNotEmpty()) {
                result = JSONObject(jsonString)
            }else{
                result= JSONObject()
                result.put("headers", JSONObject(response?.headers))
            }
            Response.success(
                result,
                HttpHeaderParser.parseCacheHeaders(response)
            )
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (je: JSONException) {
            Response.error(ParseError(je))
        }
    }

    override fun getHeaders(): MutableMap<String, String> {
        val headers = HashMap<String,String>()
        headers["Authorization"] = AuthenticationManager?.getCredentials(context).token
        return headers
    }
}