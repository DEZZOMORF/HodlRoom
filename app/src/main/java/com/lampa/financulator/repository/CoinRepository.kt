package com.lampa.financulator.repository

import android.content.Context
import com.lampa.financulator.R
import com.lampa.financulator.api.ApiService
import com.lampa.financulator.api.entity.CoinEntity
import com.lampa.financulator.api.mapper.CoinMapper
import com.lampa.financulator.manager.NetworkConnectionManager
import com.lampa.financulator.model.Coin
import com.lampa.financulator.util.ConstVal.filterStrings
import com.lampa.financulator.util.RequestErrorHandler
import com.lampa.financulator.util.RequestState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CoinRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiService: ApiService,
    private val networkConnectionManager: NetworkConnectionManager,
    private val coinMapper: CoinMapper,
    private val requestErrorHandler: RequestErrorHandler,
) {
    suspend fun getCoinList(): RequestState<List<Coin>?> {
        return try {
            when (networkConnectionManager.isConnected.value) {
                true -> {
                    val response = apiService.getCoinList()
                    if (response.isSuccessful) {
                        RequestState.Success(response.body()?.let { filterCoinList(it) })
                    } else {
                        requestErrorHandler.handleError(response)
                    }
                }
                else -> throw Exception(context.getString(R.string.network_error_no_internet_connection))
            }
        } catch (e: Exception) {
            RequestState.GeneralError(e)
        }
    }

    suspend fun getCoinById(id: String): RequestState<Coin?> {
        return try {
            when (networkConnectionManager.isConnected.value) {
                true -> {
                    val response = apiService.getCoinById(id)
                    if (response.isSuccessful) {
                        RequestState.Success(response.body()?.let { coinMapper.mapEntityToModel(it) })
                    } else {
                        requestErrorHandler.handleError(response)
                    }
                }
                else -> throw Exception(context.getString(R.string.network_error_no_internet_connection))
            }
        } catch (e: Exception) {
            RequestState.GeneralError(e)
        }
    }

    private fun filterCoinList(body: List<CoinEntity>): List<Coin> {
        return coinMapper
            .mapEntityToModel(body)
            .filter { coin ->
                !filterStrings.any {
                    coin.name?.startsWith(it) == true
                }
            }
    }
}