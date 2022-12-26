package com.dezzomorf.financulator.api.entity

import com.google.firebase.Timestamp

data class PurchaseEntity(
    val coinId: String,
    val currency: String,
    val date: Timestamp,
    val description: String?,
    val price: Float,
    val quantity: Float
)
