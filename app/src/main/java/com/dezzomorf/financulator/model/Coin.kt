package com.dezzomorf.financulator.model

import com.dezzomorf.financulator.api.entity.CurrencyEntity

data class Coin(
    val id: String?,
    val symbol: String?,
    val name: String?,
    val logo: String?,
    val currentPrice: CurrencyEntity
)
