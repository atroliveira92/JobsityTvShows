package com.jobsity.tvseries.util.exception

import java.io.IOException

sealed class JobsityException(message: String): Exception(message) {
    class ApiException(message : String, val errorCode : Int) : JobsityException(message)
    class NoInternetException(message: String) : JobsityException(message)
    class EmptyDataException(message: String): JobsityException(message)
}