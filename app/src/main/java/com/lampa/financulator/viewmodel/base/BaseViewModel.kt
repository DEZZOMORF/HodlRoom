package com.lampa.financulator.viewmodel.base

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    fun formatPrice(coinPrice: Float?): String {
        return if (coinPrice != null) {
            if (coinPrice < 1) {
                String.format("%.12f", coinPrice).trimEnd('0').replace(",", ".")
            } else {
                String.format("%.2f", coinPrice).replace(",", ".")
            }
        } else ""
    }
}