package com.dezzomorf.financulator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.DefaultPurchaseItemBinding
import com.dezzomorf.financulator.model.ChangesByPurchase
import javax.inject.Inject

class PurchasesRecyclerViewAdapter @Inject constructor() : BaseRecyclerViewAdapter<ChangesByPurchase>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemRootView: ConstraintLayout
//        when (viewType) {
//            FIRST_ITEM_TYPE -> {
//                itemRootView = FirstChangesByCoinItemBinding.inflate(
//                    LayoutInflater.from(viewGroup.context), viewGroup, false
//                ).root
//            }
//            LUST_ITEM_TYPE -> {
//                itemRootView = LastChangesByCoinItemBinding.inflate(
//                    LayoutInflater.from(viewGroup.context), viewGroup, false
//                ).root
//            }
//            SINGLE_ITEM_TYPE -> {
//                itemRootView = SingleChangesByCoinItemBinding.inflate(
//                    LayoutInflater.from(viewGroup.context), viewGroup, false
//                ).root
//            }
//            else -> {
        itemRootView = DefaultPurchaseItemBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        ).root
//            }
//        }
        return ViewHolder(itemRootView)
    }

    inner class ViewHolder(itemView: View) : BaseRecyclerViewAdapter.ViewHolder(itemView) {
        private val context = itemView.context

        private val descriptionTextView: TextView = itemView.findViewById(R.id.description_text_view_purchase_item)
        private val quantityTextView: TextView = itemView.findViewById(R.id.quantity_text_view_purchase_item)
        private val priceTextView: TextView = itemView.findViewById(R.id.price_text_view_purchase_item)
        private val sumTextView: TextView = itemView.findViewById(R.id.sum_text_view_purchase_item)
        private val profitTextView: TextView = itemView.findViewById(R.id.profit_text_view_purchase_item)

        override fun bindView() {
            val item = getList()[adapterPosition]

            if (item.description.isNullOrEmpty()) {
                descriptionTextView.isGone = true
            } else {
                descriptionTextView.text = item.description
            }
            quantityTextView.text = item.quantity.toString()
            priceTextView.text = item.price.toString()
            sumTextView.text = item.sum.toString()
            profitTextView.text = item.profitInDollars.toString() + " | " + item.profitInPercents.toString()

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