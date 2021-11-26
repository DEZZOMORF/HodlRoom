package com.lampa.financulator.model

import com.lampa.financulator.api.entity.CurrencyEntity

data class Coin(
    val id: String?,
    val symbol: String?,
    val name: String?,
    val image: String?,
    val currentPrice: CurrencyEntity?
)
