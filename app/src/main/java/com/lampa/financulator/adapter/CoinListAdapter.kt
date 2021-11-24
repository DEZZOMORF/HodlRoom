package com.lampa.financulator.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.lampa.financulator.databinding.CoinItemBinding
import com.lampa.financulator.model.Coin
import java.util.*
import javax.inject.Inject

class CoinListAdapter @Inject constructor() : RecyclerView.Adapter<CoinListAdapter.ViewHolder>(), Filterable {

    var coinList: List<Coin> = listOf()
        set(value) {
            field = value
            coinFilterList = value
        }
    var coinFilterList: List<Coin> = listOf()
    var onItemClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CoinItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView()
    }

    override fun getItemCount() = coinFilterList.size

    inner class ViewHolder(private val binding: CoinItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView() {
            binding.data = coinFilterList[adapterPosition]
            binding.item.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                coinFilterList = if (charSearch.isEmpty()) {
                    coinList
                } else {
                    val resultList: MutableList<Coin> = mutableListOf()
                    for (coin in coinList) {
                        if (coin.name?.lowercase(Locale.ROOT)?.contains(charSearch.lowercase(Locale.ROOT)) == true) {
                            resultList.add(coin)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = coinFilterList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                coinFilterList = results?.values as List<Coin>
                notifyDataSetChanged()
            }
        }
    }
}