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
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val coinRepository: CoinRepository
) : ViewModel() {

    var coinListLoadState: MutableLiveData<UiState<List<Coin>>> = MutableLiveData()
    private var coinList: List<Coin>? = null

    var filteredCoinList: MutableLiveData<List<Coin>> = MutableLiveData()

    init {
        getCoinList()
    }

    private fun getCoinList() {
        coinListLoadState.postValue(UiState.Loading)
        viewModelScope.launch {
            when (val requestState = coinRepository.getCoinList()) {
                is RequestState.Success -> {
                    requestState.data?.let {
                        coinList = it
                        coinListLoadState.postValue(UiState.Success(it))
                    }
                }
                is RequestState.RequestError -> {
                    coinListLoadState.postValue(
                        UiState.Error(
                            Exception(requestState.requestErrorModel.message)
                        )
                    )
                }
                is RequestState.GeneralError -> {
                    coinListLoadState.postValue(
                        UiState.Error(
                            Exception(requestState.exception.message)
                        )
                    )
                }
            }
        }
    }

    fun filter(charSearch: String) {
        coinList?.let {
            filteredCoinList.postValue(
                if (charSearch.isEmpty()) {
                    it
                } else {
                    val trimmedString = charSearch.trim()
                    mutableListOf<Coin>().apply {
                        for (coin in it) {
                            if (coin.name?.isContained(trimmedString) == true || coin.symbol?.isContained(trimmedString) == true) {
                                add(coin)
                            }
                        }
                    }
                }
            )
        }
    }

    private fun String.isContained(charSearch: String): Boolean {
        return this.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))
    }
}