package com.dezzomorf.financulator.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.model.CoinListCache
import com.dezzomorf.financulator.model.Purchase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.reflect.Type
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) : SharedPreferencesInterface {

    companion object {
        private const val PREFERENCES_FILE_NAME = "financulator.pref"

        private const val COIN_LIST_KEY = "coinListKey"
        private const val COIN_LIST_TIMER = 24 * 60 * 60 * 1000 //24h

        private const val COIN_KEY = "coinKey"

        private const val PURCHASES_KEY = "purchasesKey"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)

    override fun setCoinList(userId: String, coinList: List<Coin>?) {
        val coinListCache = CoinListCache(coinList, Date().time)
        val dataCopy = HashMap(_coinList)
        dataCopy[userId] = coinListCache
        _coinList = dataCopy
    }

    override fun getCoinList(userId: String): List<Coin>? {
        val coinListCache = _coinList[userId] ?: return null
        if (Date().time > coinListCache.lastUpdateDate + COIN_LIST_TIMER) return null //Timer
        return coinListCache.coinList
    }

    override fun setCoin(userId: String, coin: Coin) {
        val dataCopy = HashMap(_coin)
        val coinsForUser = HashMap(dataCopy[userId] ?: emptyMap())
        coinsForUser[coin.id] = coin
        dataCopy[userId] = coinsForUser
        _coin = dataCopy
    }

    override fun getCoin(userId: String, coinId: String): Coin? {
        return _coin[userId]?.get(coinId)
    }

    override fun setPurchases(userId: String, purchaseList: List<Purchase>) {
        val dataCopy = HashMap(_purchases)
        dataCopy[userId] = purchaseList
        _purchases = dataCopy
    }

    override fun getPurchases(userId: String): List<Purchase>? {
        return _purchases[userId]
    }

    private var _coinList: Map<String, CoinListCache>
        get() {
            val type = object : TypeToken<Map<String, CoinListCache>>() {}.type
            return getValue(type, COIN_LIST_KEY) ?: emptyMap()
        }
        set(value) = setValue(COIN_LIST_KEY, value)

    private var _coin: Map<String, Map<String, Coin>>
        get() {
            val type = object : TypeToken<Map<String, Map<String, Coin>>>() {}.type
            return getValue(type, COIN_KEY) ?: emptyMap()
        }
        set(value) = setValue(COIN_KEY, value)

    private var _purchases: Map<String, List<Purchase>>
        get() {
            val type = object : TypeToken<Map<String, List<Purchase>>>() {}.type
            return getValue(type, PURCHASES_KEY) ?: emptyMap()
        }
        set(value) = setValue(PURCHASES_KEY, value)

    // convenience
    private fun <T> getValue(type: Type, key: String): T? {
        val json = sharedPreferences.getString(key, null) ?: return null
        return Gson().fromJson(json, type)
    }

    private fun <T> setValue(key: String, value: T) {
        val json = Gson().toJson(value)
        sharedPreferences.edit {
            putString(key, json)
        }
    }
}