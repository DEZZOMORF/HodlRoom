package com.dezzomorf.financulator.manager

import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.model.Purchase

interface SharedPreferencesInterface {
    fun setCoinList(coinList: List<Coin>?)
    fun getCoinList(isOnline: Boolean): List<Coin>?

    fun setCoin(coin: Coin)
    fun getCoin(coinId: String, isOnline: Boolean): Coin?

    fun setPurchases(userId: String, purchaseList: List<Purchase>)
    fun getPurchases(userId: String): List<Purchase>?
}