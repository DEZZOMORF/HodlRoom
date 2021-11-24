package com.lampa.financulator.repository

import com.lampa.financulator.manager.NetworkConnectionManager
import com.lampa.financulator.model.Coin
import com.lampa.financulator.api.ApiService
import com.lampa.financulator.api.entity.CoinEntity
import com.lampa.financulator.api.mapper.CoinMapper
import com.lampa.financulator.util.RequestErrorHandler
import com.lampa.financulator.util.RequestState
import javax.inject.Inject

class CoinRepository @Inject constructor(
    private val apiService: ApiService,
    private val networkConnectionManager: NetworkConnectionManager,
    private val coinMapper: CoinMapper
) {
    suspend fun getCoinList(): RequestState<List<Coin>?> {
        return try {
            when (networkConnectionManager.isConnected.value) {
                true -> {
                    val response = apiService.getCoinList()
                    if (response.isSuccessful) {
                        RequestState.Success(filterCoinList(response.body()))
                    } else {
                        RequestErrorHandler().handleError(response)
                    }
                }
                else -> throw Exception(NetworkConnectionManager.MESSAGE)
            }
        } catch (e: Exception) {
            RequestState.GeneralError(e)
        }
    }

    private fun filterCoinList(body: List<CoinEntity>?): List<Coin>? {
        val filterStrings: List<String> = listOf("0.5X", "RealT Token", "1X Short", "3X Long", "3X Short", "Aave")
        return body
            ?.let {
                coinMapper.mapEntityToModel(it)
            }
            ?.filter { coin ->
                !filterStrings.any {
                    coin.name?.startsWith(it) == true
                }
            }
    }
}