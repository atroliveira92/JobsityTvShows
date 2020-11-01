package com.jobsity.tvseries.util.exception

sealed class JobsitySecurityPinException(message: String) : Exception(message){
    class PinNumberFormatException(message: String) : JobsitySecurityPinException(message)
    class EmptyPinNumberException(message: String): JobsitySecurityPinException(message)
}