package com.dezzomorf.financulator.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dezzomorf.financulator.api.entity.PurchaseEntity
import com.dezzomorf.financulator.api.mapper.PurchaseMapper
import com.dezzomorf.financulator.extensions.parallelMap
import com.dezzomorf.financulator.manager.NetworkConnectionManager
import com.dezzomorf.financulator.manager.SharedPreferencesManager
import com.dezzomorf.financulator.model.Purchase
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.base.BaseViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class DataBaseViewModel @Inject constructor(
) : BaseViewModel() {

    private val dataBase = Firebase.firestore

    @Inject
    lateinit var purchaseMapper: PurchaseMapper

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    @Inject
    lateinit var networkConnectionManager: NetworkConnectionManager

    var addPurchaseState: MutableLiveData<UiState<Unit>> = MutableLiveData()
    var getPurchasesState: MutableLiveData<UiState<List<Purchase>>> = MutableLiveData()

    companion object {
        private const val DATA_BASE_LOG_TAG = "DATA_BASE_LOG_TAG"
    }

    private fun generateId(): String {
        return java.util.UUID.randomUUID().toString()
    }

    fun addPurchase(purchase: PurchaseEntity) {
        addPurchaseState.postValue(UiState.Loading)
        auth.currentUser?.let { user ->
            if (networkConnectionManager.isConnected.value) {
                savePurchaseToDataBase(purchase, user.uid)
            } else {
                savePurchaseLater(purchase, user.uid)
            }
        }
    }

    fun getPurchases() {
        addPurchaseState.postValue(UiState.Loading)
        auth.currentUser?.let { user ->
            dataBase.collection("users")
                .document(user.uid)
                .collection("purchases")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val purchaseList = purchaseMapper.mapEntityToModel(task.result.documents)
                        sharedPreferencesManager.setPurchases(user.uid, purchaseList)
                        getPurchasesState.postValue(UiState.Success(purchaseList))
                    } else {
                        getPurchasesState.postValue(
                            UiState.Error(
                                Exception(task.exception)
                            )
                        )
                        // Post the cached data if the request is error
                        val cachedPurchases = sharedPreferencesManager.getPurchases(user.uid)
                        if (cachedPurchases != null && cachedPurchases.isNotEmpty()) {
                            getPurchasesState.postValue(UiState.Success(cachedPurchases))
                        }
                    }
                }
        }
    }

    private fun savePurchaseToDataBase(purchase: PurchaseEntity, userId: String) {
        Log.e(DATA_BASE_LOG_TAG, "start savePurchaseToDataBase")
        dataBase.collection("users")
            .document(userId)
            .collection("purchases")
            .document(generateId())
            .set(purchase)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e(DATA_BASE_LOG_TAG, "savePurchaseToDataBase: isSuccessful")
                    sharedPreferencesManager.removeSaveLaterPurchase(userId, purchase)
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

    // Save to shared preferences
    private fun savePurchaseLater(purchase: PurchaseEntity, uid: String) {
        Log.e(DATA_BASE_LOG_TAG, "savePurchaseLater")
        sharedPreferencesManager.addSaveLaterPurchase(uid, purchase)
        addPurchaseState.postValue(UiState.Success(Unit))
    }

    fun setUpIsConnectCollecting() {
        viewModelScope.launch {
            networkConnectionManager.isConnected.collect { isConnected ->
                auth.currentUser?.let { user ->
                    if (isConnected) {
                        Log.e(DATA_BASE_LOG_TAG, "internet connected")
                        val saveLaterPurchases = sharedPreferencesManager.getSaveLaterPurchases(user.uid)
                        Log.e(DATA_BASE_LOG_TAG, "$saveLaterPurchases")
                        if (!saveLaterPurchases.isNullOrEmpty()) {
                            saveLaterPurchases.parallelMap {
                                savePurchaseToDataBase(it, user.uid)
                            }
                        }
                    }
                }
            }
        }
    }
}