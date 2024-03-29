package com.dezzomorf.financulator.model

data class ChangesByCoin(
    val coin: Coin,
    val averagePrice: Float,
    val quantity: Float,
    val sum: Float,
    val profitInPercents: Float,
    val profitInDollars: Float,
)
