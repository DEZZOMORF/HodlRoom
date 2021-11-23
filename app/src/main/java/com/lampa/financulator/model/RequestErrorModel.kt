package com.lampa.financulator.model

data class RequestErrorModel(
    val errorCode: ErrorCode,
    val message: String?
)