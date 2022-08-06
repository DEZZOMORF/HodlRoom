package com.lampa.financulator.extensions

import com.lampa.financulator.api.entity.CurrencyEntity
import kotlin.reflect.full.memberProperties

fun CurrencyEntity.getPriceByPosition(position: Int): Float {
    return CurrencyEntity::class.memberProperties.map { member ->
        member.get(this)
    }[position] as Float //TODO FIX. Crush on certain coins. Float can be null. For check find ECM coin.
}