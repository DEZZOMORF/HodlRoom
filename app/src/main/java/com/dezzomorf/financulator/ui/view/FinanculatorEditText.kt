package com.dezzomorf.financulator.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.util.AttributeSet
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.dezzomorf.financulator.R

@SuppressWarnings("ResourceType")
class FinanculatorEditText(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var hintValue: String? = null
    private var backgroundValue: Drawable? = null
    private var inputType: Int
    private var textColorValue: Int
    private var alphaLength: Int
    private val set = intArrayOf(
        android.R.attr.background,  // idx 0
        android.R.attr.inputType,  // idx 1
    )

    val text: Editable
        get() {
            return inputEditText.text
        }

    private var hintTextView: TextView
    private var inputEditText: EditText
    private var rootFrameLayout: FrameLayout

    init {
        inflate(context, R.layout.layout_financulator_edit_text, this)
        hintTextView = findViewById(R.id.hint_text_view)
        inputEditText = findViewById(R.id.input_edit_text)
        rootFrameLayout = findViewById(R.id.root_frame_layout)

        val customAttributes = context.obtainStyledAttributes(attrs, R.styleable.FinanculatorEditText)
        hintValue = customAttributes.getString(R.styleable.FinanculatorEditText_hint)
        textColorValue = customAttributes.getColor(R.styleable.FinanculatorEditText_textColor, context.getColor(android.R.color.black))
        alphaLength = customAttributes.getInt(R.styleable.FinanculatorEditText_alphaLength, 12)
        customAttributes.recycle()

        val nativeAttributes = context.obtainStyledAttributes(attrs, set)
        backgroundValue = nativeAttributes.getDrawable(0)
        inputType = nativeAttributes.getInt(1, 0)
        nativeAttributes.recycle()

        hintTextView.text = hintValue
        hintTextView.setTextColor(textColorValue)
        rootFrameLayout.background = backgroundValue
        inputEditText.background = backgroundValue
        inputEditText.setTextColor(textColorValue)
        inputEditText.inputType = inputType

        this.addTextChangedListener {}
    }

    fun setText(str: String) {
        inputEditText.setText(str)
    }

    fun addTextChangedListener(action: () -> Unit) {
        val defaultAlpha = 0.5f
        val alphaCoefficient = 20f
        inputEditText.addTextChangedListener {
            action.invoke()
            when (inputEditText.text.length > alphaLength) {
                true -> hintTextView.alpha = defaultAlpha - (inputEditText.text.length - alphaLength) / alphaCoefficient
                false -> hintTextView.alpha = defaultAlpha
            }
        }
    }
}