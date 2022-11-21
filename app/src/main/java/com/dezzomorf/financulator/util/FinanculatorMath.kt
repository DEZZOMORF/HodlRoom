package com.dezzomorf.financulator.util

import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.model.Purchase

object FinanculatorMath {
    fun getAveragePrice(purchases: List<Purchase>): Double {
        return purchases.map { it.price }.average()
    }

    fun coinQuantity(purchases: List<Purchase>): Double {
        return purchases.map { it.quantity }.sum().toDouble()
    }

    fun sum(purchases: List<Purchase>, currentPrice: Float?): Double? {
        return currentPrice?.let { coinQuantity(purchases).times(it) }
    }

    fun changesInPercents(purchases: List<Purchase>, coin: Coin): Double {
        return 0.5
    }

    fun changesInDollars(purchases: List<Purchase>, coin: Coin): Double {
        return 0.5
    }
}