package com.lampa.financulator.api.mapper

import com.lampa.financulator.model.Coin
import com.lampa.financulator.api.entity.CoinEntity
import com.lampa.financulator.util.BaseMapper
import javax.inject.Inject

class CoinMapper @Inject constructor(): BaseMapper<CoinEntity, Coin>() {

    override fun mapEntityToModel(entity: CoinEntity): Coin = with(entity) {
        Coin(
            id = id,
            symbol = symbol,
            name = name
        )
    }
}