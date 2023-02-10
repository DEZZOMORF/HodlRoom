package com.dezzomorf.financulator.api.entity

import androidx.annotation.Keep
import com.google.firebase.Timestamp

@Keep
data class PurchaseEntity(
    val coinId: String,
    val currency: String,
    val date: Timestamp,
    val description: String?,
    val price: Float,
    val quantity: Float
)
