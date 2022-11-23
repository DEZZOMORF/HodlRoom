package com.dezzomorf.financulator.manager

import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.model.Purchase

interface SharedPreferencesInterface {
    fun setCoinList(coinList: List<Coin>?)
    fun getCoinList(): List<Coin>?

    fun setCoin(coin: Coin)
    fun getCoin(coinId: String): Coin?

    fun setPurchases(userId: String, purchaseList: List<Purchase>)
    fun getPurchases(userId: String): List<Purchase>?
}