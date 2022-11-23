package com.dezzomorf.financulator.model

data class Purchase(
    val purchaseId: String,
    val coinId: String,
    val currency: CurrencyName,
    val description: String?,
    val price: Float,
    val quantity: Float
)
