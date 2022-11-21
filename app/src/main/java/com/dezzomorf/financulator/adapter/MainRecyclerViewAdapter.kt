package com.dezzomorf.financulator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.DefaultCoinItemBinding
import com.dezzomorf.financulator.databinding.FirstCoinItemBinding
import com.dezzomorf.financulator.databinding.LustCoinItemBinding
import com.dezzomorf.financulator.databinding.SingleCoinItemBinding
import com.dezzomorf.financulator.extensions.loadAndSetImage
import com.dezzomorf.financulator.model.ChangesByCoin
import javax.inject.Inject

class MainRecyclerViewAdapter @Inject constructor() : BaseRecyclerViewAdapter<ChangesByCoin>() {

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
        private val coinNameTextView: TextView = itemView.findViewById(R.id.coin_name_text_view_coin_item)
        private val coinSymbolTextView: TextView = itemView.findViewById(R.id.coin_symbol_text_view_coin_item)
        private val coinLogoImageView: ImageView = itemView.findViewById(R.id.coin_logo_image_view_coin_item)
        override fun bindView() {
            val item = getList()[adapterPosition]
            //TODO
            coinNameTextView.text = item.coin.name
            coinSymbolTextView.text = item.coin.symbol
            coinLogoImageView.loadAndSetImage(item.coin.logo)
            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}