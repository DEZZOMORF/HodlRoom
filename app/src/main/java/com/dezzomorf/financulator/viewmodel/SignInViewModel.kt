package com.dezzomorf.financulator.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : BaseViewModel() {

    var signInState: MutableLiveData<UiState<Unit>> = MutableLiveData()

    fun signInWithEmailAndPassword(email: String, password: String) {
        signInState.postValue(UiState.Loading)
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                signInState.postValue(UiState.Success(Unit))
            } else {
                signInState.postValue(
                    UiState.Error(Exception(task.exception?.message))
                )
            }
        }
    }
}