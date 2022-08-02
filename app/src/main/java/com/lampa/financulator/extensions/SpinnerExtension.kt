package com.lampa.financulator.extensions

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.lampa.financulator.R

fun <T> Spinner.setUpSpinner(list: List<T>, onItemSelectedAction: (Int) -> Unit) {
    var selectedItemPosition = -1
    val adapter: ArrayAdapter<T> = object : ArrayAdapter<T>(this.context, R.layout.layout_spinner, R.id.spinner_text_view_spinner_item, list) {
        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
            return super.getDropDownView(position, null, parent).apply {
                when (position) {
                    selectedItemPosition -> setBackgroundColor(R.color.purple_200)
                    else -> setBackgroundColor(android.R.color.transparent)
                }
            }
        }
    }

    val onItemClickListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parentView: AdapterView<*>?) {}
        override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
            selectedItemPosition = position
            onItemSelectedAction.invoke(position)
        }
    }

    adapter.setDropDownViewResource(R.layout.layout_spinner_item)

    this.onItemSelectedListener = onItemClickListener
    this.adapter = adapter
}

fun Spinner.selectSpinnerValue(str: String) {
    for (i in 0 until count) {
        if (getItemAtPosition(i).toString() == str) {
            setSelection(i)
            break
        }
    }
}