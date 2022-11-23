package com.dezzomorf.financulator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.DefaultChangesByCoinItemBinding
import com.dezzomorf.financulator.extensions.format
import com.dezzomorf.financulator.extensions.formatToTwoDigits
import com.dezzomorf.financulator.extensions.loadAndSetImage
import com.dezzomorf.financulator.model.ChangesByCoin
import com.dezzomorf.financulator.model.CurrencyName
import javax.inject.Inject

class MainRecyclerViewAdapter @Inject constructor() : BaseRecyclerViewAdapter<ChangesByCoin>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        //TODO Item types layouts
        val itemRootView: ConstraintLayout
//        when (viewType) {
//            FIRST_ITEM_TYPE -> {
//                itemRootView = FirstCoinItemBinding.inflate(
//                    LayoutInflater.from(viewGroup.context), viewGroup, false
//                ).root
//            }
//            LUST_ITEM_TYPE -> {
//                itemRootView = LustCoinItemBinding.inflate(
//                    LayoutInflater.from(viewGroup.context), viewGroup, false
//                ).root
//            }
//            SINGLE_ITEM_TYPE -> {
//                itemRootView = SingleCoinItemBinding.inflate(
//                    LayoutInflater.from(viewGroup.context), viewGroup, false
//                ).root
//            }
//            else -> {
        itemRootView = DefaultChangesByCoinItemBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        ).root
//            }
//        }
        return ViewHolder(itemRootView)
    }

    inner class ViewHolder(itemView: View) : BaseRecyclerViewAdapter.ViewHolder(itemView) {
        private val context = itemView.context

        private val coinNameTextView: TextView = itemView.findViewById(R.id.coin_name_text_view_changes_by_coin_item)
        private val coinSymbolTextView: TextView = itemView.findViewById(R.id.coin_symbol_text_view_changes_by_coin_item)
        private val coinLogoImageView: ImageView = itemView.findViewById(R.id.coin_logo_image_view_changes_by_coin_item)
        private val currentPriceTextView: TextView = itemView.findViewById(R.id.current_price_text_view_changes_by_coin_item)
        private val averagePriceTextView: TextView = itemView.findViewById(R.id.average_price_text_view_changes_by_coin_item)
        private val quantityTextView: TextView = itemView.findViewById(R.id.quantity_text_view_changes_by_coin_item)
        private val sumTextView: TextView = itemView.findViewById(R.id.sum_text_view_changes_by_coin_item)
        private val changesInPercentsTextView: TextView = itemView.findViewById(R.id.changes_in_percents_text_view_changes_by_coin_item)
        private val changesInDollarsTextView: TextView = itemView.findViewById(R.id.changes_in_dollars_in_percents_text_view_changes_by_coin_item)

        override fun bindView() {
            val item = getList()[adapterPosition]
            coinLogoImageView.loadAndSetImage(item.coin.logo)
            coinNameTextView.text = item.coin.name
            coinSymbolTextView.text = item.coin.symbol
            currentPriceTextView.text = context.getString(R.string.current_price_with_value, item.coin.currentPrice[CurrencyName.USD.value].format())
            averagePriceTextView.text = context.getString(R.string.average_price_with_value, item.averagePrice.format())
            quantityTextView.text = context.getString(R.string.quantity_with_value, item.quantity.format())
            sumTextView.text = context.getString(R.string.sum_with_value, item.sum.formatToTwoDigits())
            changesInPercentsTextView.text = context.getString(R.string.changes_in_percents_with_value, item.changesInPercents.formatToTwoDigits())
            changesInDollarsTextView.text = context.getString(R.string.changes_in_dollars_with_value, item.changesInDollars.formatToTwoDigits())
            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}