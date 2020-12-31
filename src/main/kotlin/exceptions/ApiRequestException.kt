package exceptions

import java.lang.Exception

class ApiRequestException(responseCode: Int) :
    Exception("The API request has received a bad response code: $responseCode.") {
}