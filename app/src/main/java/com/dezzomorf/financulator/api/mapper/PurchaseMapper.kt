package com.dezzomorf.financulator.api.mapper

import com.dezzomorf.financulator.model.Purchase
import com.dezzomorf.financulator.util.BaseMapper
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class PurchaseMapper @Inject constructor() : BaseMapper<DocumentSnapshot, Purchase>() {

    override fun mapEntityToModel(entity: DocumentSnapshot): Purchase =
        Purchase(
            purchaseId = entity.id,
            coinId = entity.get("coinId").toString(),
            currency = entity.get("currency").toString(),
            description = entity.get("description").toString(),
            price = entity.get("price").toString().toFloat(),
            quantity = entity.get("quantity").toString().toFloat(),
        )
}