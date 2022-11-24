package com.dezzomorf.financulator.model

import java.io.Serializable

data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val logo: String?,
    val currentPrice: Map<String, Float>
) : Serializable
