package com.dezzomorf.financulator.model

data class RequestErrorModel(
    val errorCode: ErrorCode,
    val message: String?
)