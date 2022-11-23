package com.dezzomorf.financulator.util

import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.model.Purchase

object FinanculatorMath {
    //TODO All that fucking logic doesn't work!!!!!!!!!!!! THINK!THINK!THINK!
    fun getAveragePrice(purchases: List<Purchase>): Double {
        return purchases.map { it.price }.average()
    }

    fun coinQuantity(purchases: List<Purchase>): Double {
        return purchases.map { it.quantity }.sum().toDouble()
    }

    fun sum(purchases: List<Purchase>, currentPrice: Float): Double {
        return coinQuantity(purchases).times(currentPrice)
    }

    fun changesInPercents(purchases: List<Purchase>, coin: Coin): Double {
//        val sum = sum(purchases, coin.currentPrice.USD)
//        return percentageDifference(sum, coin.currentPrice.USD) - 100
        return 0.0
    }

    fun changesInDollars(purchases: List<Purchase>, coin: Coin): Double {
        val changesInPercents = changesInPercents(purchases, coin)
        val averagePrice = getAveragePrice(purchases)
        return averagePrice / 100 * changesInPercents
    }

    fun percentageDifference(listPrice: Double, actualPrice: Float): Double {
        return ((listPrice - actualPrice) / listPrice) * 100
    }
}