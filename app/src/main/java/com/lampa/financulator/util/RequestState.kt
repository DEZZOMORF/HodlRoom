package com.lampa.financulator.util

import com.lampa.financulator.model.RequestErrorModel

sealed class RequestState<out T> {

    data class Success<T>(val data: T) : RequestState<T>()

    data class RequestError(val requestErrorModel: RequestErrorModel) : RequestState<Nothing>()

    data class GeneralError(val exception: Exception) : RequestState<Nothing>()

}