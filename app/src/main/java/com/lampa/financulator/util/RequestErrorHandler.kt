package com.lampa.financulator.util

import com.lampa.financulator.model.ErrorCode
import com.lampa.financulator.model.RequestErrorModel
import org.json.JSONObject
import retrofit2.Response

class RequestErrorHandler {

    fun <T> handleError(response: Response<*>? = null): RequestState<T> {
        return when (response?.code()) {
            400 -> {
                RequestState.RequestError(
                    RequestErrorModel(
                        ErrorCode.ERROR_400,
                        response.errorBody()?.string()?.messageParser() ?: ErrorCode.ERROR_400.toString()
                    )
                )
            }
            else -> {
                RequestState.RequestError(
                    RequestErrorModel(
                        ErrorCode.SOMETHING_WENT_WRONG,
                        response?.errorBody()?.string()?.messageParser() ?: ErrorCode.SOMETHING_WENT_WRONG.toString()
                    )
                )
            }
        }
    }

    private fun String.messageParser() : String {
        return JSONObject(this).get("message").toString()
    }
}