package com.dezzomorf.financulator.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.base.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmailVerificationViewModel @Inject constructor() : BaseViewModel() {

    var sendEmailVerificationState: MutableLiveData<UiState<FirebaseUser>> = MutableLiveData()
    var isEmailVerifiedState: MutableLiveData<UiState<Unit>> = MutableLiveData()

    fun checkIsEmailVerified() {
        isEmailVerifiedState.postValue(UiState.Loading)
        auth.currentUser?.let { user ->
            user.reload().addOnCompleteListener() { task ->
                if (user.isEmailVerified) {
                    isEmailVerifiedState.postValue(UiState.Success(Unit))
                } else {
                    isEmailVerifiedState.postValue(
                        UiState.Error(Exception(task.exception?.message))
                    )
                }
            }
        }
    }

    fun sendEmailVerification() {
        sendEmailVerificationState.postValue(UiState.Loading)
        auth.currentUser?.let { user ->
            user.sendEmailVerification().addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    sendEmailVerificationState.postValue(UiState.Success(user))
                } else {
                    sendEmailVerificationState.postValue(
                        UiState.Error(Exception(task.exception?.message))
                    )
                }
            }
        }
    }
}