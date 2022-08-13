package com.dezzomorf.financulator.extensions

import android.util.Patterns
import java.util.regex.Pattern

fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun CharSequence?.isValidPassword(): Boolean {
    val passwordPattern = "^(?=\\S+$).{4,}$"
    val pattern: Pattern = Pattern.compile(passwordPattern)
    val matcher = pattern.matcher(this ?: "")
    return !isNullOrEmpty() && matcher.matches()
}