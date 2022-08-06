package com.lampa.financulator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.lampa.financulator.R
import com.lampa.financulator.databinding.DefaultCoinItemBinding
import com.lampa.financulator.databinding.FirstCoinItemBinding
import com.lampa.financulator.databinding.LustCoinItemBinding
import com.lampa.financulator.databinding.SingleCoinItemBinding
import com.lampa.financulator.model.Coin
import javax.inject.Inject

class CoinListRecyclerViewAdapter @Inject constructor() : BaseRecyclerViewAdapter<Coin>() {

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
            SINGLE_ITEM_TYPE -> {
                itemRootView = SingleCoinItemBinding.inflate(
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

    inner class ViewHolder(itemView: View) : BaseRecyclerViewAdapter.ViewHolder(itemView) {
        private val coinName: TextView = itemView.findViewById(R.id.coin_name_text_view_coin_item)
        private val coinSymbol: TextView = itemView.findViewById(R.id.coin_symbol_text_view_coin_item)
        override fun bindView() {
            with(getList()[adapterPosition]) {
                coinName.text = this.name
                coinSymbol.text = this.symbol
                itemView.setOnClickListener {
                    onItemClick(getList()[adapterPosition])
                }
            }
        }
    }
}