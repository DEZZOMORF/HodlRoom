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
        auth.currentUser?.let { user ->
            coinState.postValue(UiState.Loading)
            viewModelScope.launch {

                val cachedCoin = coinRepository.getCachedCoin(user.uid, coinId)
                if (cachedCoin != null) {
                    coinState.postValue(UiState.Success(cachedCoin))
                }

                when (val requestState = coinRepository.getCoinById(coinId)) {
                    is RequestState.Success -> {
                        requestState.data?.let {
                            coinRepository.setCoinToCache(user.uid, it)
                            coinState.postValue(UiState.Success(it))
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