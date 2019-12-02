package pl.polsl.homeorganizer.register

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class CustomRequest(
    method: Int,
    url: String,
    jsonRequest: JSONObject,
    listener: Response.Listener<JSONObject>,
    errorListener: Response.ErrorListener
) : JsonObjectRequest(method,url,jsonRequest,listener,errorListener){

    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
        return try {
            val jsonString = String(
                response?.data ?: ByteArray(0),
                Charset.forName(HttpHeaderParser.parseCharset(response?.headers)))
            var result: JSONObject? = null
            if (jsonString.length > 0) {
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
}