package com.jobsity.tvseries.util.exception

import java.io.IOException

sealed class JobsityListException(message: String): IOException(message) {
    class ApiListException(message : String, val errorCode : Int) : JobsityListException(message)
    class NoInternetListException(message: String) : JobsityListException(message)
    class EmptyDataListException(message: String): JobsityListException(message)
}