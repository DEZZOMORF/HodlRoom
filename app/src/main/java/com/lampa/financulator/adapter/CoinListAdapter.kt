package com.lampa.financulator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.lampa.financulator.R
import com.lampa.financulator.databinding.DefaultCoinItemBinding
import com.lampa.financulator.databinding.FirstCoinItemBinding
import com.lampa.financulator.databinding.LustCoinItemBinding
import com.lampa.financulator.model.Coin
import javax.inject.Inject

class CoinListAdapter @Inject constructor() : RecyclerView.Adapter<CoinListAdapter.ViewHolder>() {

    var coinList: List<Coin> = listOf()
    var onItemClickListener: ((String) -> Unit)? = null

    companion object {
        const val DEFAULT_ITEM_TYPE = 0
        const val FIRST_ITEM_TYPE = 1
        const val LUST_ITEM_TYPE = 2
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemRootView: ConstraintLayout
        when (viewType) {
            FIRST_ITEM_TYPE -> {
                itemRootView = FirstCoinItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                ).root
            }
            LUST_ITEM_TYPE -> {
                itemRootView = LustCoinItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                ).root
            }
            else -> {
                itemRootView = DefaultCoinItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                ).root
            }
        }
        return ViewHolder(itemRootView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView()
    }

    override fun getItemCount() = coinList.size

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> FIRST_ITEM_TYPE
            itemCount - 1 -> LUST_ITEM_TYPE
            else -> DEFAULT_ITEM_TYPE
        }
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val coinName: TextView = view.findViewById(R.id.coin_name_text_view_coin_item)
        private val coinSymbol: TextView = view.findViewById(R.id.coin_symbol_text_view_coin_item)
        fun bindView() {
            with(coinList[adapterPosition]) {
                coinName.text = this.name
                coinSymbol.text = this.symbol
                view.setOnClickListener {
                    coinList[adapterPosition].id?.let { id -> onItemClickListener?.invoke(id) }
                }
            }
        }
    }
}