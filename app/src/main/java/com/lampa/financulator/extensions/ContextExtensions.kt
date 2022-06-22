package com.lampa.financulator.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lampa.financulator.R
import com.lampa.financulator.util.ResourcesCompat

inline val Context.resourcesCompat: ResourcesCompat
    get() = ResourcesCompat(this)

fun Context.showSuccess(
    @StringRes
    message: Int,
    onDismiss: (() -> Unit)?,
    onShow: (() -> Unit)?
): AlertDialog? {
    if (!isActivityValid()) {
        return null
    }
    val dialog = MaterialAlertDialogBuilder(this)
        .setTitle(R.string.dialog_text_success)
        .setIcon(R.drawable.ic_baseline_check_circle_24)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok) { _, _ -> }
        .setOnDismissListener {
            onDismiss?.invoke()
        }
        .create()
    dialog.setOnShowListener {
        onShow?.invoke()
    }
    dialog.show()
    return dialog
}

fun Context.showSuccess(
    message: CharSequence
): AlertDialog? {
    return showSuccess(message, null, null)
}

fun Context.showSuccess(
    @StringRes
    message: Int
): AlertDialog? {
    return showSuccess(message, null, null)
}

fun Context.showSuccess(
    message: CharSequence,
    onDismiss: (() -> Unit)?,
    onShow: (() -> Unit)?
): AlertDialog? {
    if (!isActivityValid()) {
        return null
    }
    val dialog = MaterialAlertDialogBuilder(this)
        .setTitle(R.string.dialog_text_success)
        .setIcon(R.drawable.ic_baseline_check_circle_24)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok) { _, _ -> }
        .setOnDismissListener {
            onDismiss?.invoke()
        }
        .create()

    dialog.setOnShowListener {
        onShow?.invoke()
    }
    dialog.show()
    return dialog
}

fun Context.showError(
    @StringRes errorMessage: Int,
    onDismiss: (() -> Unit)?,
    onShow: (() -> Unit)?
): AlertDialog? {
    if (!isActivityValid()) {
        return null
    }
    val dialog = MaterialAlertDialogBuilder(this)
        .setIcon(R.drawable.ic_baseline_error_24)
        .setTitle(R.string.dialog_text_error)
        .setMessage(errorMessage)
        .setPositiveButton(android.R.string.ok) { _, _ -> }
        .setOnDismissListener {
            onDismiss?.invoke()
        }
        .create()
    dialog.setOnShowListener {
        onShow?.invoke()
    }
    dialog.show()
    return dialog
}

fun Context.showError(
    errorMessage: CharSequence,
): AlertDialog? {
    return showError(errorMessage, null, null)
}

fun Context.showError(
    @StringRes errorMessage: Int
): AlertDialog? {
    return showError(errorMessage, null, null)
}

fun Context.showError(
    errorMessage: CharSequence,
    onDismiss: (() -> Unit)?,
    onShow: (() -> Unit)?
): AlertDialog? {
    if (!isActivityValid()) {
        return null
    }
    val dialog = MaterialAlertDialogBuilder(this)
        .setIcon(R.drawable.ic_baseline_error_24)
        .setTitle(R.string.dialog_text_error)
        .setMessage(errorMessage)
        .setPositiveButton(android.R.string.ok) { _, _ -> }
        .setOnDismissListener {
            onDismiss?.invoke()
        }
        .create()
    dialog.setOnShowListener {
        onShow?.invoke()
    }
    dialog.show()
    return dialog
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager: InputMethodManager = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showKeyboard(view: View) {
    val inputMethodManager: InputMethodManager = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    if (!isActivityValid()) {
        return
    }
    Toast.makeText(this, text, duration).show()
}

fun Context.showToast(@StringRes textResourceId: Int, duration: Int = Toast.LENGTH_SHORT) {
    if (!isActivityValid()) {
        return
    }
    Toast.makeText(this, textResourceId, duration).show()
}

fun Context.isActivityValid(): Boolean {
    val activity = this as? Activity
    if (activity != null && (!activity.window.decorView.isShown || activity.isDestroyed || activity.isFinishing)) {
        return false
    }
    return true
}
