package com.dezzomorf.financulator.api.entity

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class CurrentPriceEntity(
    @SerializedName("current_price")
    @Expose
    val currentPrice: Map<String, Float>
)
