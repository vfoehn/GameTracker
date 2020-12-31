package requests

import exceptions.ApiRequestException
import exceptions.RecurrentFailedRequestsException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.system.exitProcess

abstract class Request(val apiKey: String) {

    val MAX_FAILED_REQUEST = 5
    val BACKOFF_TIME: Long = 2000

    abstract fun getUrl(): String

    abstract fun setHeader(con: HttpURLConnection)

    fun sendSingleRequest(): JSONObject {
        var response: JSONObject? = null
        val urlString = "${getUrl()}"
        // println("urlString: $urlString")
        val url = URL(urlString)
        val con = url.openConnection() as HttpURLConnection
        setHeader(con)
        con.requestMethod = "GET"

        val responseCode = con.responseCode
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
        var inputLine: String? = ""
        val responseString = StringBuffer()
        while (responseReader.readLine().also { inputLine = it } != null) {
            responseString.append(inputLine)
        }
        responseReader.close()

        response = JSONObject(responseString.toString())
        return response
    }

    fun sendRequest(): JSONObject {
        var accountResponse: JSONObject? = null
        var numFailedRequests = 0
        while (accountResponse == null) {
            try {
                accountResponse = sendSingleRequest()
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