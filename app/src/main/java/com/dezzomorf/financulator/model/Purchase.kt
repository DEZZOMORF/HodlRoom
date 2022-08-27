package com.dezzomorf.financulator.model

data class Purchase(
    val id: String = java.util.UUID.randomUUID().toString(),
    val coinId: String,
    val currency: String,
    val description: String?,
    val price: Float,
    val quantity: Float
)
