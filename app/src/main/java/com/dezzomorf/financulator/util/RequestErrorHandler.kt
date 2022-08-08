package com.dezzomorf.financulator.util

import android.content.Context
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.model.ErrorCode
import com.dezzomorf.financulator.model.RequestErrorModel
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestErrorHandler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun <T> handleError(response: Response<*>? = null): RequestState<T> {
        return when (response?.code()) {
            429 -> {
                RequestState.RequestError(
                    RequestErrorModel(
                        ErrorCode.ERROR_429,
                        context.getString(R.string.network_error_429)
                    )
                )
            }
            else -> {
                RequestState.RequestError(
                    RequestErrorModel(
                        ErrorCode.SOMETHING_WENT_WRONG,
                        context.getString(R.string.network_error_default)
                    )
                )
            }
        }
    }
}