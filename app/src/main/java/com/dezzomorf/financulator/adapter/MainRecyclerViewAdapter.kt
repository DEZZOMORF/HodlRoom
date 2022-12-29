package com.dezzomorf.financulator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.DefaultChangesByCoinItemBinding
import com.dezzomorf.financulator.databinding.FirstChangesByCoinItemBinding
import com.dezzomorf.financulator.databinding.LastChangesByCoinItemBinding
import com.dezzomorf.financulator.databinding.SingleChangesByCoinItemBinding
import com.dezzomorf.financulator.extensions.format
import com.dezzomorf.financulator.extensions.formatToTwoDigits
import com.dezzomorf.financulator.extensions.loadAndSetImage
import com.dezzomorf.financulator.extensions.resourcesCompat
import com.dezzomorf.financulator.model.ChangesByCoin
import com.dezzomorf.financulator.model.CurrencyName
import javax.inject.Inject

class MainRecyclerViewAdapter @Inject constructor() : BaseRecyclerViewAdapter<ChangesByCoin>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemRootView: ConstraintLayout
        when (viewType) {
            FIRST_ITEM_TYPE -> {
                itemRootView = FirstChangesByCoinItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                ).root
            }
            LUST_ITEM_TYPE -> {
                itemRootView = LastChangesByCoinItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                ).root
            }
            SINGLE_ITEM_TYPE -> {
                itemRootView = SingleChangesByCoinItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                ).root
            }
            else -> {
                itemRootView = DefaultChangesByCoinItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                ).root
            }
        }
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
        private val profitInPercentsTextView: TextView = itemView.findViewById(R.id.profit_in_percents_text_view_changes_by_coin_item)
        private val profitInDollarsTextView: TextView = itemView.findViewById(R.id.profit_in_dollars_in_percents_text_view_changes_by_coin_item)

        override fun bindView() {
            val item = getList()[adapterPosition]
            coinLogoImageView.loadAndSetImage(item.coin.logo)
            coinNameTextView.text = item.coin.name
            coinSymbolTextView.text = item.coin.symbol
            currentPriceTextView.text = context.getString(R.string.current_price_with_value, item.coin.currentPrice[CurrencyName.USD.value].format(), CurrencyName.USD)
            averagePriceTextView.text = context.getString(R.string.average_price_with_value, item.averagePrice.format(), CurrencyName.USD)
            quantityTextView.text = context.getString(R.string.quantity_with_value, item.quantity.format())
            sumTextView.text = context.getString(R.string.sum_with_value, item.sum.formatToTwoDigits(), CurrencyName.USD)
            profitInPercentsTextView.text = context.getString(R.string.profit_in_percents_with_value, item.profitInPercents.formatToTwoDigits())
            profitInDollarsTextView.text = context.getString(R.string.profit_in_currency_with_value, item.profitInDollars.formatToTwoDigits(), CurrencyName.USD)

            profitInPercentsTextView.setCompoundDrawablesWithIntrinsicBounds( null, null,  when {
                item.profitInPercents > 0f -> context.resourcesCompat.getDrawable(R.drawable.ic_baseline_arrow_drop_up_24)
                item.profitInPercents < 0f -> context.resourcesCompat.getDrawable(R.drawable.ic_baseline_arrow_drop_down_24)
                else -> return
            }, null)

            profitInDollarsTextView.setCompoundDrawablesWithIntrinsicBounds( null, null,  when {
                item.profitInPercents > 0f -> context.resourcesCompat.getDrawable(R.drawable.ic_baseline_arrow_drop_up_24)
                item.profitInPercents < 0f -> context.resourcesCompat.getDrawable(R.drawable.ic_baseline_arrow_drop_down_24)
                else -> return
            }, null)

            itemView.setOnClickListener {
                onItemClick(item)
            }
            itemView.setOnLongClickListener {
                onItemLongClick(item)
                true
            }
        }
    }
}