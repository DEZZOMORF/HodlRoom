package com.lampa.financulator.util

import androidx.recyclerview.widget.DiffUtil

class FinanculatorDiffUtil<E>(
    private val oldList: List<E>,
    private val newList: List<E>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]?.equals(newList[newItemPosition]) ?: false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]?.equals(newList[newItemPosition]) ?: false
    }
}