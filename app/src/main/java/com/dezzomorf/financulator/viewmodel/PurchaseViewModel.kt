package com.dezzomorf.financulator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.repository.CoinRepository
import com.dezzomorf.financulator.util.RequestState
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val coinRepository: CoinRepository
) : BaseViewModel() {

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