package com.dezzomorf.financulator.api.entity

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class CoinEntity(
    @SerializedName("id")
    @Expose
    val id: String,

    @SerializedName("symbol")
    @Expose
    val symbol: String,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("image")
    @Expose
    val image: CoinLogoEntity?,

    @SerializedName("market_data")
    @Expose
    val marketData: CurrentPriceEntity?,
)
