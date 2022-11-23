package com.dezzomorf.financulator.model

data class ChangesByCoin(
    val coin: Coin,
    val averagePrice: Float,
    val quantity: Float,
    val sum: Float,
    val changesInPercents: Float,
    val changesInDollars: Float,
)
