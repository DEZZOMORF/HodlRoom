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

    var filteredCoinList: List<Coin> = listOf()
    var coinList: List<Coin> = listOf()
        set(value) {
            field = value
            filteredCoinList = value
        }
    var onItemClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CoinItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView()
    }

    override fun getItemCount() = filteredCoinList.size

    inner class ViewHolder(private val binding: CoinItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView() {
            binding.data = filteredCoinList[adapterPosition]
            binding.item.setOnClickListener {
                filteredCoinList[adapterPosition].id?.let { id -> onItemClickListener?.invoke(id) }
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filteredCoinList = if (charSearch.isEmpty()) {
                    coinList
                } else {
                    val resultList: MutableList<Coin> = mutableListOf()
                    for (coin in coinList) {
                        if (coin.name?.isContained(charSearch) == true || coin.symbol?.isContained(charSearch) == true) {
                            resultList.add(coin)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredCoinList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredCoinList = results?.values as List<Coin>
                notifyDataSetChanged()
            }
        }
    }

    fun String.isContained(charSearch: String): Boolean {
        return this.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))
    }
}