package com.dezzomorf.financulator.util

import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.model.CurrencyName
import com.dezzomorf.financulator.model.Purchase

class FinanculatorMath(
    private val coin: Coin,
    private val tetherData: Coin,
    private val purchases: List<Purchase>
) {
    fun getAveragePrice(): Float {
        return purchases.map {
            // Get price in usd(tether)
            val currencyPrice = tetherData.currentPrice[it.currency.value] ?: 0f
            it.price / currencyPrice
        }.average().toFloat()
    }

    fun coinQuantity(): Float {
        return purchases.map { it.quantity }.sum()
    }

    fun sum(): Float {
        return coin.currentPrice[CurrencyName.USD.value]?.times(coinQuantity()) ?: 0f
    }

    fun profitInPercents(): Float {
        val averagePrice = getAveragePrice()
        return percentageDifference(averagePrice, coin.currentPrice[CurrencyName.USD.value] ?: 0f)
    }

    fun profitInDollars(): Float {
        return getAveragePrice() * coinQuantity() / 100 * profitInPercents()
    }

    private fun percentageDifference(listPrice: Float, actualPrice: Float): Float {
        return ((actualPrice - listPrice) / listPrice) * 100
    }
}