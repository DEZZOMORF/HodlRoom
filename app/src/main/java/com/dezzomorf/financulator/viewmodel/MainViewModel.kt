package com.dezzomorf.financulator.viewmodel

import com.dezzomorf.financulator.repository.CoinRepository
import com.dezzomorf.financulator.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val coinRepository: CoinRepository
) : DataBaseViewModel() {

}