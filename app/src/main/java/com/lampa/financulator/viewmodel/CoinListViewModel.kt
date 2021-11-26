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
class CoinListViewModel @Inject constructor(
    private val coinRepository: CoinRepository
) : ViewModel() {

    var coinListState: MutableLiveData<UiState<List<Coin>>> = MutableLiveData()

    init {
        getCoinList()
    }

    private fun getCoinList() {
        coinListState.postValue(UiState.Loading)
        viewModelScope.launch {
            when (val requestState = coinRepository.getCoinList()) {
                is RequestState.Success -> {
                    requestState.data?.let { coinListState.postValue(UiState.Success(it)) }
                }
                is RequestState.RequestError -> {
                    coinListState.postValue(
                        UiState.Error(
                            Exception(requestState.requestErrorModel.message)
                        )
                    )
                }
                is RequestState.GeneralError -> {
                    coinListState.postValue(
                        UiState.Error(
                            Exception(requestState.exception.message)
                        )
                    )
                }
            }
        }
    }
}