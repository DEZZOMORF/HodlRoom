package com.dezzomorf.financulator.extensions

import android.view.View

fun View.hideKeyboardOnTap() {
    isClickable = true
    isFocusable = true
    isFocusableInTouchMode = true
    setOnFocusChangeListener { view, _ ->
        context.hideKeyboard(view)
    }
}

fun View.scaleInAnimation(duration: Long = 300, completion: (() -> Unit)? = null) {
    scaleX = 0f
    scaleY = 0f
    visibility = View.VISIBLE
    animate()
        .scaleX(1f)
        .scaleY(1f)
        .setDuration(duration)
        .withEndAction {
            completion?.let {
                it()
            }
        }
}