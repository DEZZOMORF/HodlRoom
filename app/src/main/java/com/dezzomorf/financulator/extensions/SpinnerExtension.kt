package com.dezzomorf.financulator.extensions

import android.widget.Spinner

fun Spinner.selectSpinnerValue(str: String) {
    for (i in 0 until count) {
        if (getItemAtPosition(i).toString() == str) {
            setSelection(i)
            break
        }
    }
}