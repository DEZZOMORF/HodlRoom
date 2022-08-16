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

fun View.scaleOutAnimation(duration: Long = 300, completion: (() -> Unit)? = null) {
    scaleX = 1f
    scaleY = 1f
    animate()
        .scaleX(0f)
        .scaleY(0f)
        .setDuration(duration)
        .withEndAction {
            completion?.let {
                it()
            }
        }
}

fun View.scaleAnimation(duration: Long = 300, completion: (() -> Unit)? = null) {
    this.scaleOutAnimation(duration / 2) {
        completion?.let {
            it()
        }
        this.scaleInAnimation(duration / 2) {}
    }
}

fun View.horizontalSqueezeInAnimation(duration: Long = 300, completion: (() -> Unit)? = null) {
    scaleX = 0f
    visibility = View.VISIBLE
    animate()
        .scaleX(1f)
        .setDuration(duration)
        .withEndAction {
            completion?.let {
                it()
            }
        }
}

fun View.horizontalSqueezeOutAnimation(duration: Long = 300, completion: (() -> Unit)? = null) {
    scaleX = 1f
    animate()
        .scaleX(0f)
        .setDuration(duration)
        .withEndAction {
            completion?.let {
                it()
            }
        }
}

fun View.horizontalSqueezeAnimation(duration: Long = 300, completion: (() -> Unit)? = null) {
    this.horizontalSqueezeOutAnimation(duration / 2) {
        completion?.let {
            it()
        }
        this.horizontalSqueezeInAnimation(duration / 2) {}
    }
}