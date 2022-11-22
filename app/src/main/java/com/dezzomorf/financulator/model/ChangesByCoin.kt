package com.dezzomorf.financulator.model

data class ChangesByCoin(
    val coin: Coin,
    val averagePrice: String,
    val quantity: String,
    val sum: String,
    val changesInPercents: String,
    val changesInDollars: String,
)
