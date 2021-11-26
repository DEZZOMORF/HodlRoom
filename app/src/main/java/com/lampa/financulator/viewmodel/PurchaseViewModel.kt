package com.lampa.financulator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lampa.financulator.model.Coin
import com.lampa.financulator.repository.CoinRepository
import com.lampa.financulator.util.RequestState
import com.lampa.financulator.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val coinRepository: CoinRepository
) : ViewModel() {

    var coinState: MutableLiveData<UiState<Coin>> = MutableLiveData()

    fun getCoinById(id: String) {
        coinState.postValue(UiState.Loading)
        viewModelScope.launch {
            when (val requestState = coinRepository.getCoinById(id)) {
                is RequestState.Success -> {
                    requestState.data?.let { coinState.postValue(UiState.Success(it)) }
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