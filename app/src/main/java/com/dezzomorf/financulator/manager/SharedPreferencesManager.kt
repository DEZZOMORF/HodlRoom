package com.dezzomorf.financulator.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.dezzomorf.financulator.api.entity.PurchaseEntity
import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.model.CoinListCache
import com.dezzomorf.financulator.model.Purchase
import com.dezzomorf.financulator.model.RatingFlowData
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
        private const val COIN_LIST_TIMER = 3 * 24 * 60 * 60 * 1000 //3d

        private const val COIN_KEY = "coinKey"
        private const val COIN_TIMER = 1 * 60 * 60 * 1000 //1h

        private const val PURCHASES_KEY = "purchasesKey"

        private const val SAVE_LATER_PURCHASES_KEY = "saveLaterPurchasesKey"

        private const val APP_RATE = "ratingFlowData"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)

    //Let the user use the app 5 days, to get a good feel of it.
    private val defaultRatingFlowData = RatingFlowData(false, Date().time + 1000 * 60 * 60 * 24 * 5)

    override fun setCoinList(coinList: List<Coin>?) {
        val coinListCache = CoinListCache(coinList, Date().time)
        _coinList = coinListCache
    }

    override fun getCoinList(isOnline: Boolean): List<Coin>? {
        val coinListCache = _coinList ?: return null
        if (Date().time > coinListCache.lastUpdateDate + COIN_LIST_TIMER && isOnline) return null //Timer and internet check
        return coinListCache.coinList
    }

    override fun setCoin(coin: Coin) {
        val dataCopy = HashMap(_coin)
        dataCopy[coin.id] = Pair(Gson().toJson(coin), Date().time)
        _coin = dataCopy
    }

    override fun getCoin(coinId: String, isOnline: Boolean): Coin? {
        val coinCache = _coin[coinId] ?: return null
        if (Date().time > coinCache.second + COIN_TIMER && isOnline) return null //Timer and internet check
        return Gson().fromJson(coinCache.first, Coin::class.java)
    }

    override fun setPurchases(userId: String, purchaseList: List<Purchase>) {
        val dataCopy = HashMap(_purchases)
        dataCopy[userId] = purchaseList
        _purchases = dataCopy
    }

    override fun getPurchases(userId: String): List<Purchase>? {
        return _purchases[userId]
    }

    override fun addSaveLaterPurchase(userId: String, purchaseEntity: PurchaseEntity) {
        val dataCopy = HashMap(_saveLaterPurchases)

        val newList = mutableListOf<PurchaseEntity>().also { list ->
            dataCopy[userId]?.let { list.addAll(it) }
            list.add(purchaseEntity)
        }
        dataCopy[userId] = newList

        _saveLaterPurchases = dataCopy
    }

    override fun getSaveLaterPurchases(userId: String): List<PurchaseEntity>? {
        return _saveLaterPurchases[userId]
    }

    override fun removeSaveLaterPurchase(userId: String, purchaseEntity: PurchaseEntity) {
        val dataCopy = HashMap(_saveLaterPurchases)
        val userSaveLaterPurchaseList = dataCopy[userId]?.toMutableList()
        val savedPurchase = userSaveLaterPurchaseList?.find { it.date.seconds == purchaseEntity.date.seconds } ?: return
        userSaveLaterPurchaseList.remove(savedPurchase)
        dataCopy[userId] = userSaveLaterPurchaseList
        _saveLaterPurchases = dataCopy
    }

    override fun getRatingFlowData(): RatingFlowData {
        val dataCopy = _ratingFlowData
        return if (dataCopy != null) {
            dataCopy
        } else {
            _ratingFlowData = defaultRatingFlowData
            defaultRatingFlowData
        }
    }

    override fun appRated() {
        val dataCopy = getRatingFlowData()
        dataCopy.isRatingFlowFinished = true
        _ratingFlowData = dataCopy
    }

    override fun resetLastRatingRequest() {
        val dataCopy = getRatingFlowData()
        dataCopy.lastRatingRequest = Date().time
        _ratingFlowData = dataCopy
    }

    private var _coinList: CoinListCache?
        get() {
            val type = object : TypeToken<CoinListCache>() {}.type
            return getValue(type, COIN_LIST_KEY)
        }
        set(value) = setValue(COIN_LIST_KEY, value)

    private var _coin: Map<String, Pair<String, Long>>
        get() {
            val type = object : TypeToken<Map<String, Pair<String, Long>>>() {}.type
            return getValue(type, COIN_KEY) ?: emptyMap()
        }
        set(value) = setValue(COIN_KEY, value)

    private var _purchases: Map<String, List<Purchase>>
        get() {
            val type = object : TypeToken<Map<String, List<Purchase>>>() {}.type
            return getValue(type, PURCHASES_KEY) ?: emptyMap()
        }
        set(value) = setValue(PURCHASES_KEY, value)

    private var _saveLaterPurchases: Map<String, List<PurchaseEntity>>
        get() {
            val type = object : TypeToken<Map<String, List<PurchaseEntity>>>() {}.type
            return getValue(type, SAVE_LATER_PURCHASES_KEY) ?: emptyMap()
        }
        set(value) = setValue(SAVE_LATER_PURCHASES_KEY, value)

    private var _ratingFlowData: RatingFlowData?
        get() {
            val type = object : TypeToken<RatingFlowData>() {}.type
            return getValue(type, APP_RATE)
        }
        set(value) = setValue(APP_RATE, value)

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