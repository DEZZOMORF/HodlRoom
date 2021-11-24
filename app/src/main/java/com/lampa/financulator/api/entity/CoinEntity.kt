package com.lampa.financulator.api.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CoinEntity(
    @SerializedName("id")
    @Expose
    val id: String?,

    @SerializedName("symbol")
    @Expose
    val symbol: String?,

    @SerializedName("name")
    @Expose
    val name: String?,
)
