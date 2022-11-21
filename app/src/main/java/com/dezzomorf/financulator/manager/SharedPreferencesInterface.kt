package com.dezzomorf.financulator.manager

import com.dezzomorf.financulator.model.Coin

interface SharedPreferencesInterface {
    fun setCoinList(userId: String, coinList: List<Coin>?)
    fun getCoinList(userId: String): List<Coin>?
}