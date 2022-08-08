package com.dezzomorf.financulator.api

import com.dezzomorf.financulator.api.entity.CoinEntity
import com.dezzomorf.financulator.util.NetworkUrls
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET(NetworkUrls.COINS_LIST)
    suspend fun getCoinList(): Response<MutableList<CoinEntity>>

    @GET(NetworkUrls.COIN)
    suspend fun getCoinById(@Path("id") id: String): Response<CoinEntity>

}