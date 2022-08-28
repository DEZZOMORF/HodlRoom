package com.dezzomorf.financulator.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dezzomorf.financulator.model.Purchase
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.base.BaseViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class DataBaseViewModel @Inject constructor() : BaseViewModel() {

    private val dataBase = Firebase.firestore

    var addPurchaseState: MutableLiveData<UiState<Nothing?>> = MutableLiveData()

    private fun generateId(): String{
        return java.util.UUID.randomUUID().toString()
    }

    fun addPurchase(purchase: Purchase) {
        addPurchaseState.postValue(UiState.Loading)
        auth.currentUser?.let { user ->
            dataBase.collection("users")
                .document(user.uid)
                .collection("purchases")
                .document(generateId())
                .set(purchase)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        addPurchaseState.postValue(UiState.Success(null))
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
}