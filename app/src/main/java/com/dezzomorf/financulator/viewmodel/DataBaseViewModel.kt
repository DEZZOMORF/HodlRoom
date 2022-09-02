package com.dezzomorf.financulator.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dezzomorf.financulator.api.entity.PurchaseEntity
import com.dezzomorf.financulator.api.mapper.PurchaseMapper
import com.dezzomorf.financulator.model.Purchase
import com.dezzomorf.financulator.repository.CoinRepository
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.base.BaseViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class DataBaseViewModel @Inject constructor() : BaseViewModel() {

    private val dataBase = Firebase.firestore
    @Inject lateinit var purchaseMapper: PurchaseMapper

    var addPurchaseState: MutableLiveData<UiState<Unit>> = MutableLiveData()
    var getPurchasesState: MutableLiveData<UiState<List<Purchase?>>> = MutableLiveData()

    private fun generateId(): String{
        return java.util.UUID.randomUUID().toString()
    }

    fun addPurchase(purchase: PurchaseEntity) {
        addPurchaseState.postValue(UiState.Loading)
        auth.currentUser?.let { user ->
            dataBase.collection("users")
                .document(user.uid)
                .collection("purchases")
                .document(generateId())
                .set(purchase)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        addPurchaseState.postValue(UiState.Success(Unit))
                    } else {
                        addPurchaseState.postValue(
                            UiState.Error(
                                Exception(task.exception)
                            )
                        )
                    }
                }
        }
    }

    fun getPurchases(){
        addPurchaseState.postValue(UiState.Loading)
        auth.currentUser?.let { user ->
            dataBase.collection("users")
                .document(user.uid)
                .collection("purchases")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        getPurchasesState.postValue(
                            UiState.Success(
                                purchaseMapper.mapEntityToModel(task.result.documents)
                            )
                        )
                    } else {
                        getPurchasesState.postValue(
                            UiState.Error(
                                Exception(task.exception)
                            )
                        )
                    }
                }
        }
    }
}