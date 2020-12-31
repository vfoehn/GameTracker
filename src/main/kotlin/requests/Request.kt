package requests

import exceptions.ApiRequestException
import exceptions.RecurrentFailedRequestsException
import org.apache.log4j.Logger
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.system.exitProcess

// A Request represents an HTTP GET request. The URL, header and query parameters can be customized.
// The class provides logic to deal with different (successful and failed) HTTP responses.
abstract class Request(val apiKey: String) {

    var logger: Logger = Logger.getLogger(Request::class.java)

    val MAX_FAILED_REQUEST = 5
    val BACKOFF_TIME: Long = 2000

    abstract fun getUrl(): String

    abstract fun setHeader(con: HttpURLConnection)

    fun sendSingleRequest(encapsulateJSONArray: Boolean): JSONObject {
        val response: JSONObject?
        val urlString = getUrl()
        // println("urlString: $urlString")
        val url = URL(urlString)
        val con = url.openConnection() as HttpURLConnection
        setHeader(con)
        con.requestMethod = "GET"

        val responseCode = con.responseCode
        logger.info("responseCode: $responseCode")
        if (responseCode == 404) {
            // The results are empty. This can be the case if the filtering criteria are too strict.
            return JSONObject()
        }
        if (responseCode != 429 && responseCode in 400..500) {
            // The requested data is not returned by the server because the request is faulty or it lacks permission.
            // Note: Response code 429 represents "Rate limit exceeded". So there is still hope of obtaining the
            // requested data.
            val exception = ApiRequestException(responseCode)
            exception.printStackTrace()
            exitProcess(-1)
        }

        val responseReader = BufferedReader(InputStreamReader(con.inputStream))
        val responseString = StringBuffer()
        var inputLine = responseReader.readLine()
        while (inputLine != null) {
            responseString.append(inputLine)
            inputLine = responseReader.readLine()
        }
        responseReader.close()

        response = if (encapsulateJSONArray) {
            JSONObject().put("versions", JSONArray(responseString.toString())) as JSONObject
        } else {
            JSONObject(responseString.toString())
        }
        logger.info("response: $response")
        return response
    }

    // The boolean "encapsulateJSONArray" indicates that the HTTP response is of type JSONArray and should
    // be encapsulated inside a JSONObject in order to satisfy the return types of the functions.
    fun sendRequest(encapsulateJSONArray: Boolean = false): JSONObject {
        var accountResponse: JSONObject? = null
        var numFailedRequests = 0
        while (accountResponse == null) {
            try {
                accountResponse = sendSingleRequest(encapsulateJSONArray)
                numFailedRequests = 0
            } catch (e: Exception) {
                numFailedRequests++
                if (numFailedRequests >= MAX_FAILED_REQUEST) {
                    val exception = RecurrentFailedRequestsException(numFailedRequests)
                    exception.printStackTrace()
                    exitProcess(-1)
                }

                Thread.sleep(BACKOFF_TIME)
            }
        }

        return accountResponse // Smart-cast to non-nullable type.
    }
}