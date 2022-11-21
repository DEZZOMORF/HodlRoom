package com.dezzomorf.financulator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dezzomorf.financulator.extensions.parallelMap
import com.dezzomorf.financulator.model.ChangesByCoin
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

    var changesByCoinState: MutableLiveData<UiState<MutableList<ChangesByCoin>>> = MutableLiveData()

    fun summaryChangesByCoins(purchases: List<Purchase>) {
        changesByCoinState.postValue(UiState.Loading)
        viewModelScope.launch {
            val coinIdList = purchases.map { it.coinId }.distinct()
            val requestStateList = coinIdList.parallelMap { id -> coinRepository.getCoinById(id) }.toList()
            var errorMessage: String? = null
            requestStateList.forEach { requestState ->
                when (requestState) {
                    is RequestState.RequestError -> {
                        errorMessage = requestState.requestErrorModel.message
                        return@forEach
                    }
                    is RequestState.GeneralError -> {
                        errorMessage = requestState.exception.message
                        return@forEach
                    }
                    else -> {}
                }
            }
            if (errorMessage != null) {
                changesByCoinState.postValue(
                    UiState.Error(
                        Exception(errorMessage)
                    )
                )
            } else {
                val changeByCoinList: MutableList<ChangesByCoin> = mutableListOf()
                requestStateList.map { requestState ->
                    val coin = (requestState as RequestState.Success).data
                    if (coin?.id != null) {
                        val purchasesByCoin = purchases.filter { it.coinId == coin.id }
                        changeByCoinList.add(
                            ChangesByCoin(
                                coin = coin,
                                averagePrice = getAveragePrice(purchasesByCoin),
                                quantity = coinQuantity(purchasesByCoin),
                                sum = sum(purchasesByCoin, coin.currentPrice?.USD),
                                changesInPercents = changesInPercents(purchasesByCoin, coin),
                                changesInDollars = changesInDollars(purchasesByCoin, coin)
                            )
                        )
                    }
                }
                changesByCoinState.postValue(
                    UiState.Success(changeByCoinList)
                )
            }
        }
    }
}