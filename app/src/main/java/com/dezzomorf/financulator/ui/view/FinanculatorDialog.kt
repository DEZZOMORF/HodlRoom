package com.dezzomorf.financulator.ui.view

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.extensions.resourcesCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FinanculatorDialog(private val context: Context, private val content: List<FinanculatorDialogItem>) {

    data class FinanculatorDialogItem(
        val title: String,
        val action: () -> Unit
    )

    fun createAndShow() {
        MaterialAlertDialogBuilder(context)
            .setCustomTitle(TextView(context).apply {
                setText(R.string.options)
                gravity = Gravity.CENTER
                textSize = 20f
                setTextColor(context.resourcesCompat.getColor(R.color.black))
                val margins = context.resourcesCompat.pixelsFromDimension(R.dimen.dialog_title_content_margin)
                setPadding(margins, margins, margins, margins)
            })
            .create()
            .apply {
                setView(getDialogLayout(this))
                window?.setBackgroundDrawableResource(R.drawable.bg_round_border)
            }
            .show()
    }

    private fun getDialogLayout(dialog: AlertDialog): LinearLayout {
        val options = content.map { it.title }

        return LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL

            for (i in options.indices) {
                addView(createSeparateLine())
                addView(
                    TextView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setPadding(0, 40, 0, 40)
                        }
                        gravity = Gravity.CENTER
                        text = options[i]
                        textSize = 16f
                        background = context.resourcesCompat.getDrawable(R.drawable.ripple_default)
                        setTextColor(context.resourcesCompat.getColor(R.color.purple_500))
                        setOnClickListener {
                            content[i].action.invoke()
                            dialog.cancel()
                        }
                    }
                )
            }
        }
    }

    private fun createSeparateLine() = View(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            2
        )
        background = context.resourcesCompat.getDrawable(R.color.teal_200)
    }
}