package com.lampa.financulator.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lampa.financulator.util.FinanculatorDiffUtil

abstract class BaseRecyclerViewAdapter<E> : RecyclerView.Adapter<BaseRecyclerViewAdapter.ViewHolder>() {

    companion object {
        const val DEFAULT_ITEM_TYPE = 0
        const val FIRST_ITEM_TYPE = 1
        const val SINGLE_ITEM_TYPE = 2
        const val LUST_ITEM_TYPE = 3
    }

    private var list: List<E> = listOf()
    lateinit var onItemClick: (E) -> Unit

    fun getList(): List<E> = list

    fun setList(newList: List<E>) {
        list = newList
        notifyDataSetChanged()
    }

    fun setListWithAnimation(newList: List<E>) {
        when {
            getList().size < 1000 -> {
                val diffUtil = FinanculatorDiffUtil(getList(), newList)
                val diffResults = DiffUtil.calculateDiff(diffUtil)
                list = newList
                diffResults.dispatchUpdatesTo(this)
            }
            else -> {
                setList(newList)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView()
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            itemCount == 1 -> SINGLE_ITEM_TYPE
            position == 0 -> FIRST_ITEM_TYPE
            position == itemCount - 1 -> LUST_ITEM_TYPE
            else -> DEFAULT_ITEM_TYPE
        }
    }

    override fun getItemCount() = getList().size

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bindView() {}
    }
}
