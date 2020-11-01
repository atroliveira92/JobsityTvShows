package com.jobsity.tvseries.util.exception

sealed class JobsityListException(message: String): Exception(message) {
    class ApiListException(message : String, val errorCode : Int) : JobsityListException(message)
    class NoInternetListException(message: String) : JobsityListException(message)
    class EmptyDataListException(message: String): JobsityListException(message)
}