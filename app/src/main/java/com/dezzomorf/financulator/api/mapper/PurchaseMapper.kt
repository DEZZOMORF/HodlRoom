package com.dezzomorf.financulator.api.mapper

import com.dezzomorf.financulator.model.CurrencyName
import com.dezzomorf.financulator.model.Purchase
import com.dezzomorf.financulator.util.BaseMapper
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*
import javax.inject.Inject

class PurchaseMapper @Inject constructor() : BaseMapper<DocumentSnapshot, Purchase>() {

    override fun mapEntityToModel(entity: DocumentSnapshot): Purchase =
        Purchase(
            purchaseId = entity.id,
            coinId = entity.get("coinId").toString(),
            currency = CurrencyName.from(entity.get("currency").toString().lowercase()),
            description = entity.get("description").toString(),
            date = entity.getTimestamp("date") ?: Timestamp(Date()),
            price = entity.get("price").toString().toFloat(),
            quantity = entity.get("quantity").toString().toFloat(),
        )
}