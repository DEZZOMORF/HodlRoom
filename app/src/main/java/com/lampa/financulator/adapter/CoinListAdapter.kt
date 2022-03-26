package com.lampa.financulator.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lampa.financulator.databinding.CoinItemBinding
import com.lampa.financulator.model.Coin
import javax.inject.Inject

class CoinListAdapter @Inject constructor() : RecyclerView.Adapter<CoinListAdapter.ViewHolder>() {

    var coinList: List<Coin> = listOf()
    var onItemClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CoinItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView()
    }

    override fun getItemCount() = coinList.size

    inner class ViewHolder(private val binding: CoinItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView() {
            binding.data = coinList[adapterPosition]
            binding.item.setOnClickListener {
                coinList[adapterPosition].id?.let { id -> onItemClickListener?.invoke(id) }
            }
        }
    }
}