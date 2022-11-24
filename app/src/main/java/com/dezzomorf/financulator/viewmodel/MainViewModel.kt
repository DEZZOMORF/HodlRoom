package com.dezzomorf.financulator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dezzomorf.financulator.extensions.parallelMap
import com.dezzomorf.financulator.model.ChangesByCoin
import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.model.Purchase
import com.dezzomorf.financulator.repository.CoinRepository
import com.dezzomorf.financulator.util.ConstVal.TETHER
import com.dezzomorf.financulator.util.FinanculatorMath
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
            val tetherData = getTether()
            val changesByCoin = formatDataToChangesByCoin(coinsData, purchases, tetherData)
            changesByCoinState.postValue(
                UiState.Success(changesByCoin)
            )
        }
    }

    // Get cached coin data or request new
    private suspend fun getCoinsData(coinIdList: List<String>): List<Coin?> {
        return coinIdList.parallelMap { coinId ->
            return@parallelMap coinRepository.getCachedCoin(coinId)
                ?: when (val coinRequestState = coinRepository.getCoinById(coinId)) {
                    is RequestState.Success -> {
                        coinRequestState.data?.let { coin ->
                            coinRepository.setCoinToCache(coin)
                            coin
                        }
                    }
                    else -> null
                }
        }.toList()
    }

    private suspend fun getTether(): Coin? {
        return coinRepository.getCachedCoin(TETHER)
            ?: when (val coinRequestState = coinRepository.getCoinById(TETHER)) {
                is RequestState.Success -> {
                    coinRequestState.data?.let { coin ->
                        coinRepository.setCoinToCache(coin)
                        coin
                    }
                }
                else -> null
            }
    }

    private fun formatDataToChangesByCoin(coinList: List<Coin?>, purchases: List<Purchase>, tetherData: Coin?): List<ChangesByCoin> {
        val changesByCoinList: MutableList<ChangesByCoin> = mutableListOf()
        coinList.forEach { coin ->
            if (coin != null && tetherData != null) {
                val purchasesByCoin = purchases.filter { purchase ->
                    purchase.coinId == coin.id
                }
                val financulatorMath = FinanculatorMath(coin, tetherData,purchasesByCoin)
                changesByCoinList.add(
                    ChangesByCoin(
                        coin = coin,
                        averagePrice = financulatorMath.getAveragePrice(),
                        quantity = financulatorMath.coinQuantity(),
                        sum = financulatorMath.sum(),
                        changesInPercents = financulatorMath.changesInPercents(),
                        changesInDollars = financulatorMath.changesInDollars()
                    )
                )
            }
        }
        return changesByCoinList
    }
}