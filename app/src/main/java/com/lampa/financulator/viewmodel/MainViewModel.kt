package com.lampa.financulator.viewmodel

import androidx.lifecycle.ViewModel
import com.lampa.financulator.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val coinRepository: CoinRepository
) : ViewModel()