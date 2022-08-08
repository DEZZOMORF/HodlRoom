package com.dezzomorf.financulator.extensions

fun Float?.formatPrice(): String {
    return if (this != null && this != 0f) {
        if (this < 1) {
            String.format("%.12f", this).trimEnd('0').replace(",", ".")
        } else {
            String.format("%.2f", this).replace(",", ".")
        }
    } else "-"
}