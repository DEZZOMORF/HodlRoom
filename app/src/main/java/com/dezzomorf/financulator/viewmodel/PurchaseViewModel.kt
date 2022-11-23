package com.dezzomorf.financulator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.repository.CoinRepository
import com.dezzomorf.financulator.util.RequestState
import com.dezzomorf.financulator.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val coinRepository: CoinRepository
) : DataBaseViewModel() {

    var coinState: MutableLiveData<UiState<Coin>> = MutableLiveData()

    fun getCoinById(coinId: String) {
        coinState.postValue(UiState.Loading)
        viewModelScope.launch {
            val cachedCoin = coinRepository.getCachedCoin(coinId)
            if (cachedCoin != null) {
                coinState.postValue(UiState.Success(cachedCoin))
            } else {
                when (val requestState = coinRepository.getCoinById(coinId)) {
                    is RequestState.Success -> {
                        requestState.data?.let { coin ->
                            coinRepository.setCoinToCache(coin)
                            coinState.postValue(UiState.Success(coin))
                        }
                    }
                    is RequestState.RequestError -> {
                        coinState.postValue(
                            UiState.Error(
                                Exception(requestState.requestErrorModel.message)
                            )
                        )
                    }
                    is RequestState.GeneralError -> {
                        coinState.postValue(
                            UiState.Error(
                                Exception(requestState.exception.message)
                            )
                        )
                    }
                }
            }
        }
    }
}