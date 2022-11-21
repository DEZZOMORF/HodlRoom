package com.dezzomorf.financulator.model

data class ChangesByCoin(
    val coin: Coin,
    val averagePrice: Double?,
    val quantity: Double?,
    val sum: Double?,
    val changesInPercents: Double?,
    val changesInDollars: Double?,
)
