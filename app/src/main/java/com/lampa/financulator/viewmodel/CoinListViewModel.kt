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

    var getCoinListState: MutableLiveData<UiState<List<Coin>?>> = MutableLiveData()

    fun getCoinList() {
        getCoinListState.postValue(UiState.Loading)
        viewModelScope.launch {
            when (val requestState = coinRepository.getCoinList()) {
                is RequestState.Success -> {
                    getCoinListState.postValue(UiState.Success(requestState.data))
                }
                is RequestState.RequestError -> {
                    getCoinListState.postValue(
                        UiState.Error(
                            Exception(requestState.requestErrorModel.message)
                        )
                    )
                }
                is RequestState.GeneralError -> {
                    getCoinListState.postValue(
                        UiState.Error(
                            Exception(requestState.exception.message)
                        )
                    )
                }
            }
        }
    }
}