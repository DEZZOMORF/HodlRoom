package com.dezzomorf.financulator.api.mapper

import com.dezzomorf.financulator.api.entity.CoinEntity
import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.util.BaseMapper
import javax.inject.Inject

class CoinMapper @Inject constructor(): BaseMapper<CoinEntity, Coin>() {

    override fun mapEntityToModel(entity: CoinEntity): Coin = with(entity) {
        Coin(
            id = id,
            symbol = symbol?.uppercase(),
            name = name,
            logo = image?.large,
            currentPrice = marketData?.currentPrice ?: emptyMap()
        )
    }
}