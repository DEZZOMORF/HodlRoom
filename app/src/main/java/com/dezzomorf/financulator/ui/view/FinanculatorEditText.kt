package com.dezzomorf.financulator.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.dezzomorf.financulator.R

@SuppressWarnings("ResourceType")
class FinanculatorEditText(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private var hint: String? = null
    private var inputBackground: Drawable? = null
    private var inputType: Int
    private var textColorValue: Int
    private var alphaLength: Int
    private var errorText: String? = null
    private var errorTextColor: Int
    private var includeErrorText: Boolean
    private var errorTextVisibility: Int
    private val set = intArrayOf(
        android.R.attr.inputType,  // idx 0
    )

    val text: Editable
        get() {
            return inputEditText.text
        }

    private var hintTextView: TextView
    private var errorTextView: TextView
    private var inputEditText: EditText
    private var inputLayout: FrameLayout

    init {
        inflate(context, R.layout.layout_financulator_edit_text, this)
        hintTextView = findViewById(R.id.hint_text_view_financulator_edit_text)
        errorTextView = findViewById(R.id.error_text_view_financulator_edit_text)
        inputEditText = findViewById(R.id.input_edit_text_financulator_edit_text)
        inputLayout = findViewById(R.id.input_layout)

        val customAttributes = context.obtainStyledAttributes(attrs, R.styleable.FinanculatorEditText)
        hint = customAttributes.getString(R.styleable.FinanculatorEditText_hint)
        textColorValue = customAttributes.getColor(R.styleable.FinanculatorEditText_textColor, context.getColor(android.R.color.black))
        inputBackground = customAttributes.getDrawable(R.styleable.FinanculatorEditText_inputBackground)
        alphaLength = customAttributes.getInt(R.styleable.FinanculatorEditText_alphaLength, 10)
        errorText = customAttributes.getString(R.styleable.FinanculatorEditText_errorText)
        errorTextColor = customAttributes.getColor(R.styleable.FinanculatorEditText_errorTextColor, context.getColor(android.R.color.black))
        errorTextVisibility = customAttributes.getInt(R.styleable.FinanculatorEditText_errorTextVisibility, View.GONE)
        includeErrorText = customAttributes.getBoolean(R.styleable.FinanculatorEditText_includeErrorText, false)
        customAttributes.recycle()

        val nativeAttributes = context.obtainStyledAttributes(attrs, set)
        inputType = nativeAttributes.getInt(0, 0)
        nativeAttributes.recycle()

        hintTextView.text = hint
        hintTextView.setTextColor(textColorValue)
        inputLayout.background = inputBackground
        inputEditText.background = inputBackground
        inputEditText.setTextColor(textColorValue)
        inputEditText.inputType = inputType
        errorTextView.text = errorText
        errorTextView.setTextColor(errorTextColor)
        errorTextView.visibility = errorTextVisibility

        addTextChangedListener {}
        setUpErrorTextVisibility()
    }

    fun setText(str: String) {
        inputEditText.setText(str)
    }

    private fun setUpErrorTextVisibility() {
        errorTextView.visibility = when (includeErrorText) {
            true -> View.INVISIBLE
            false -> View.GONE
        }
    }

    fun addTextChangedListener(action: (Editable) -> Unit) {
        val defaultAlpha = 0.5f
        val alphaCoefficient = 20f
        inputEditText.addTextChangedListener {
            it?.let {  action.invoke(it) }
            when (inputEditText.text.length > alphaLength) {
                true -> hintTextView.alpha = defaultAlpha - (inputEditText.text.length - alphaLength) / alphaCoefficient
                false -> hintTextView.alpha = defaultAlpha
            }
        }
        if (includeErrorText) setErrorTextVisibility(false)
    }

    fun setErrorTextVisibility(isVisible: Boolean) {
        if (includeErrorText) {
            errorTextView.visibility = when (isVisible) {
                true -> View.VISIBLE
                false -> View.INVISIBLE
            }
        }
    }
}