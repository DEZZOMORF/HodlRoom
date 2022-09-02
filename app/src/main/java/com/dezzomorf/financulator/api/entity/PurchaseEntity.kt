package com.dezzomorf.financulator.api.entity

data class PurchaseEntity(
    val coinId: String,
    val currency: String,
    val description: String?,
    val price: Float,
    val quantity: Float
)
