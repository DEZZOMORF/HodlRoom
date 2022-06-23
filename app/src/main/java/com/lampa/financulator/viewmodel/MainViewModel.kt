package com.lampa.financulator.viewmodel

import com.lampa.financulator.repository.CoinRepository
import com.lampa.financulator.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val coinRepository: CoinRepository
) : BaseViewModel()