package com.dezzomorf.financulator.api.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CoinLogoEntity(

    @SerializedName("thumb")
    @Expose
    val thumb: String?,

    @SerializedName("small")
    @Expose
    val small: String?,

    @SerializedName("large")
    @Expose
    val large: String?
)