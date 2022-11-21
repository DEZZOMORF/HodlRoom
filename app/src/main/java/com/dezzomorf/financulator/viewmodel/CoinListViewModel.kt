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
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val coinRepository: CoinRepository
) : BaseViewModel() {

    var coinListLoadState: MutableLiveData<UiState<List<Coin>>> = MutableLiveData()
    private var coinList: List<Coin>? = null

    var filteredCoinList: MutableLiveData<List<Coin>> = MutableLiveData()

    init {
        getCoinList()
    }

    private fun getCoinList() {
        auth.currentUser?.let { user ->
            coinListLoadState.postValue(UiState.Loading)
            viewModelScope.launch {
                val cachedCoinList = coinRepository.getCachedCoinList(user.uid)
                if (cachedCoinList != null && cachedCoinList.isNotEmpty()) {
                    coinList = cachedCoinList
                    coinListLoadState.postValue(UiState.Success(cachedCoinList))
                } else {
                    when (val requestState = coinRepository.getCoinList()) {
                        is RequestState.Success -> {
                            requestState.data?.let {
                                coinList = it
                                coinRepository.setCoinListToCache(user.uid, it)
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
        }
    }

    fun filter(charSearch: String) {
        coinList?.let { coinList ->
            filteredCoinList.postValue(
                if (charSearch.isEmpty()) {
                    coinList
                } else {
                    val trimmedString = charSearch.trim()
                    coinList.filter { coin ->
                        coin.name?.isContained(trimmedString) == true || coin.symbol?.isContained(trimmedString) == true
                    }
                }
            )
        }
    }

    private fun String.isContained(charSearch: String): Boolean {
        return this.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))
    }
}