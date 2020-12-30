import java.lang.Exception

class RecurrentFailedRequestsException(numFailedRequests: Int) : Exception("The poller has experienced $numFailedRequests failed requests in a row.") {

}