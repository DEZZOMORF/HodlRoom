package com.dezzomorf.financulator.repository

import android.content.Context
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.api.ApiService
import com.dezzomorf.financulator.api.entity.CoinEntity
import com.dezzomorf.financulator.api.mapper.CoinMapper
import com.dezzomorf.financulator.manager.NetworkConnectionManager
import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.util.ConstVal.filterStrings
import com.dezzomorf.financulator.util.RequestErrorHandler
import com.dezzomorf.financulator.util.RequestState
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