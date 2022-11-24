package com.dezzomorf.financulator.adapter

interface RecyclerViewCallback {
    fun onItemClick(position: Int)
    fun onItemLongClick(position: Int) {}
}
