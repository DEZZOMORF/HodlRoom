package com.dezzomorf.financulator.model

import com.google.firebase.Timestamp

data class Purchase(
    val purchaseId: String,
    val coinId: String,
    val currency: CurrencyName,
    val description: String?,
    val date: Timestamp,
    val price: Float,
    val quantity: Float
)
