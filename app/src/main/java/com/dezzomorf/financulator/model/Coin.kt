package com.dezzomorf.financulator.model

data class Coin(
    val id: String?,
    val symbol: String?,
    val name: String?,
    val logo: String?,
    val currentPrice: Map<String, Float>
)
