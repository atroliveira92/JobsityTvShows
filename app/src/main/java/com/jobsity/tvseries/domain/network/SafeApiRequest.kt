package com.jobsity.tvseries.domain.network


import com.jobsity.tvseries.util.exception.JobsityListException.ApiListException
import retrofit2.Response
import java.lang.StringBuilder

abstract class SafeApiRequest {


    suspend fun <T: Any> apiRequest(call: suspend () -> Response<T>) : T {

        val response = call.invoke()

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()
            val message = StringBuilder()
            message.append(error)
            message.append("\n")
            message.append("Error code ${response.code()}")

            throw ApiListException(message.toString(), response.code())
        }
    }
}