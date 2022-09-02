package com.dezzomorf.financulator.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor() : BaseViewModel() {

    var sendResetPasswordMessageState: MutableLiveData<UiState<Unit>> = MutableLiveData()

    fun sendResetPasswordMessage(email: String) {
        sendResetPasswordMessageState.postValue(UiState.Loading)
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sendResetPasswordMessageState.postValue(UiState.Success(Unit))
            } else {
                sendResetPasswordMessageState.postValue(
                    UiState.Error(Exception(task.exception?.message))
                )
            }
        }
    }
}