package com.dezzomorf.financulator.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : BaseViewModel() {

    var signUpState: MutableLiveData<UiState<Unit>> = MutableLiveData()

    fun createUserWithEmailAndPassword(email: String, password: String) {
        signUpState.postValue(UiState.Loading)
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                signUpState.postValue(UiState.Success(Unit))
            } else {
                signUpState.postValue(
                    UiState.Error(Exception(task.exception?.message))
                )
            }
        }
    }
}