package com.dezzomorf.financulator.extensions

fun Float?.format(): String {
    return if (this != null && this != 0f) {
        if (this < 1) {
            String.format("%.12f", this).trimEnd('0').replace(",", ".")
        } else {
            this.formatToTwoDigits()
        }
    } else ""
}

fun Float?.formatToTwoDigits(): String {
    return if (this?.isNaN() == true) {
        "0"
    } else {
        String.format("%.2f", this).replace(",", ".")
    }
}