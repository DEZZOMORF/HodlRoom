package com.dezzomorf.financulator.model

data class ChangesByPurchase(
    val purchaseId: String,
    val description: String?,
    val price: Float,
    val currency: String,
    val quantity: Float,
    val sum: Float,
    val profitInPercents: Float,
    val profitInDollars: Float,
)
