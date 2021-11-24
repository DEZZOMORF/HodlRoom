package com.lampa.financulator.api

import com.lampa.financulator.api.entity.CoinEntity
import com.lampa.financulator.util.NetworkUrls
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(NetworkUrls.COINS_LIST)
    suspend fun getCoinList(): Response<List<CoinEntity>>

}