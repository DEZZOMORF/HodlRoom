package com.dezzomorf.financulator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.DefaultPurchaseItemBinding
import com.dezzomorf.financulator.databinding.FirstPurchaseItemBinding
import com.dezzomorf.financulator.databinding.LastPurchaseItemBinding
import com.dezzomorf.financulator.databinding.SinglePurchaseItemBinding
import com.dezzomorf.financulator.extensions.format
import com.dezzomorf.financulator.extensions.formatToTwoDigits
import com.dezzomorf.financulator.extensions.resourcesCompat
import com.dezzomorf.financulator.model.ChangesByPurchase
import javax.inject.Inject

class PurchasesRecyclerViewAdapter @Inject constructor() : BaseRecyclerViewAdapter<ChangesByPurchase>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemRootView: ConstraintLayout
        when (viewType) {
            FIRST_ITEM_TYPE -> {
                itemRootView = FirstPurchaseItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                ).root
            }
            LUST_ITEM_TYPE -> {
                itemRootView = LastPurchaseItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                ).root
            }
            SINGLE_ITEM_TYPE -> {
                itemRootView = SinglePurchaseItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                ).root
            }
            else -> {
                itemRootView = DefaultPurchaseItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                ).root
            }
        }
        return ViewHolder(itemRootView)
    }

    inner class ViewHolder(itemView: View) : BaseRecyclerViewAdapter.ViewHolder(itemView) {
        private val context = itemView.context

        private val counterTextView: TextView = itemView.findViewById(R.id.counter_text_view_purchase_item)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.description_text_view_purchase_item)
        private val quantityTextView: TextView = itemView.findViewById(R.id.quantity_text_view_purchase_item)
        private val priceTextView: TextView = itemView.findViewById(R.id.price_text_view_purchase_item)
        private val sumTextView: TextView = itemView.findViewById(R.id.sum_text_view_purchase_item)
        private val profitTextView: TextView = itemView.findViewById(R.id.profit_text_view_purchase_item)

        override fun bindView() {
            val item = getList()[adapterPosition]
            val itemPosition = adapterPosition + 1

            if (item.description.isEmpty()) descriptionTextView.visibility = View.GONE
            descriptionTextView.text = item.description
            counterTextView.text = itemPosition.toString()
            quantityTextView.text = context.getString(R.string.quantity_with_value, item.quantity.format())
            priceTextView.text = context.resourcesCompat.getString(R.string.purchase_price_with_value, item.price.format(), item.currency)
            sumTextView.text = context.getString(R.string.sum_with_value, item.sum.formatToTwoDigits(), item.currency)
            profitTextView.text = context.getString(
                R.string.profit_with_value,
                item.profitInPercents.formatToTwoDigits(),
                item.profitInDollars.formatToTwoDigits(),
                item.currency
            )

            when {
                item.profitInPercents > 0f -> profitTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    context.resourcesCompat.getDrawable(R.drawable.ic_baseline_arrow_drop_up_24),
                    null
                )
                item.profitInPercents < 0f -> profitTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    context.resourcesCompat.getDrawable(R.drawable.ic_baseline_arrow_drop_down_24),
                    null
                )
                else -> {}
            }

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