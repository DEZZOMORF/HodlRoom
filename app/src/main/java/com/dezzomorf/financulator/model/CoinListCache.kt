package com.dezzomorf.financulator.model

import java.io.Serializable

data class CoinListCache(
    val coinList: List<Coin>?,
    val lastUpdateDate: Long
): Serializable
