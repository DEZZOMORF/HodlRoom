package com.lampa.financulator.extensions

import android.text.Editable

fun Editable.stringToFloatOrZero(): Float{
    val string = toString()
    return if (string == "") {
        0f
    } else {
        string.toFloat()
    }
}