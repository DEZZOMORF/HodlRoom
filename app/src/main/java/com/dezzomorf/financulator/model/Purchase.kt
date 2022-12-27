package com.dezzomorf.financulator.model

import com.google.firebase.Timestamp

data class Purchase(
    val purchaseId: String,
    val coinId: String,
    val currency: CurrencyName,
    val description: String?,
    val date: Timestamp,
    val price: Float,
    val quantity: Float
) {
    fun profitInPercents(coin: Coin) = ((coin.currentPrice[this.currency.value]?.minus(this.price))?.div(this.price))?.times(100) ?: 0f
    fun profitInDollars(coin: Coin) = this.price * this.quantity / 100 * profitInPercents(coin)
    fun sum() = this.price * this.quantity
}
