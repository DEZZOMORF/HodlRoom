package com.dezzomorf.financulator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dezzomorf.financulator.extensions.format
import com.dezzomorf.financulator.extensions.formatToTwoDigits
import com.dezzomorf.financulator.extensions.parallelMap
import com.dezzomorf.financulator.model.ChangesByCoin
import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.model.Purchase
import com.dezzomorf.financulator.repository.CoinRepository
import com.dezzomorf.financulator.util.FinanculatorMath.changesInDollars
import com.dezzomorf.financulator.util.FinanculatorMath.changesInPercents
import com.dezzomorf.financulator.util.FinanculatorMath.coinQuantity
import com.dezzomorf.financulator.util.FinanculatorMath.getAveragePrice
import com.dezzomorf.financulator.util.FinanculatorMath.sum
import com.dezzomorf.financulator.util.RequestState
import com.dezzomorf.financulator.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val coinRepository: CoinRepository
) : DataBaseViewModel() {

    var changesByCoinState: MutableLiveData<UiState<List<ChangesByCoin>>> = MutableLiveData()

    fun summaryChangesByCoins(purchases: List<Purchase>) {
        changesByCoinState.postValue(UiState.Loading)
        viewModelScope.launch {
            val coinIdList = purchases.map { it.coinId }.distinct()
            val coinsData = getCoinsData(coinIdList)
            val changesByCoin = formatDataToChangesByCoin(coinsData, purchases)
            changesByCoinState.postValue(
                UiState.Success(changesByCoin)
            )
        }
    }

    // Get cached coin data or request new
    private suspend fun getCoinsData(coinIdList: List<String>): List<Coin?> {
        return coinIdList.parallelMap { coinId ->
            val cashedCoin = sharedPreferencesManager.getCoin(coinId)
            return@parallelMap if (cashedCoin != null) {
                sharedPreferencesManager.getCoin(coinId)
            } else {
                when (val coinRequestState = coinRepository.getCoinById(coinId)) {
                    is RequestState.Success -> {
                        coinRequestState.data?.let { coin ->
                            sharedPreferencesManager.setCoin(coin)
                            coin
                        }
                    }
                    else -> null
                }
            }
        }.toList()
    }

    private fun formatDataToChangesByCoin(coinList: List<Coin?>, purchases: List<Purchase>): List<ChangesByCoin> {
        val changesByCoinList: MutableList<ChangesByCoin> = mutableListOf()
        coinList.forEach { cachedCoin ->
            if (cachedCoin?.id != null) {
                val purchasesByCoin = purchases.filter { it.coinId == cachedCoin.id }
                changesByCoinList.add(
                    ChangesByCoin(
                        coin = cachedCoin,
                        averagePrice = getAveragePrice(purchasesByCoin).toFloat().format(),
                        quantity = coinQuantity(purchasesByCoin).toFloat().format(),
                        sum = sum(purchasesByCoin, cachedCoin.currentPrice.USD).toFloat().formatToTwoDigits(),
                        changesInPercents = changesInPercents(purchasesByCoin, cachedCoin).toFloat().formatToTwoDigits(),
                        changesInDollars = changesInDollars(purchasesByCoin, cachedCoin).toFloat().formatToTwoDigits()
                    )
                )
            }
        }
        return changesByCoinList
    }
}