package com.lampa.financulator.extensions

import android.widget.ArrayAdapter
import android.widget.Spinner
import com.lampa.financulator.R

fun <T> Spinner.setList(list: List<T>) {
    val adapter: ArrayAdapter<T> = ArrayAdapter<T>(
        this.context,
        R.layout.layout_spinner,
        R.id.spinner_text_view,
        list
    )
    adapter.setDropDownViewResource(R.layout.layout_spinner_item)
    this.adapter = adapter
}