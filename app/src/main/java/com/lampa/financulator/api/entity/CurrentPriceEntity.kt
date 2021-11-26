package com.lampa.financulator.api.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CurrentPriceEntity(
    @SerializedName("current_price")
    @Expose
    val currentPrice: CurrencyEntity?
)
