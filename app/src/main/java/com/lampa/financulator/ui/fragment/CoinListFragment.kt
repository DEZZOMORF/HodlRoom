package com.lampa.financulator.ui.fragment

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lampa.financulator.R
import com.lampa.financulator.adapter.CoinListAdapter
import com.lampa.financulator.databinding.FragmentCoinListBinding
import com.lampa.financulator.ui.fragment.base.BaseFragment
import com.lampa.financulator.util.ConstVal
import com.lampa.financulator.util.UiState
import com.lampa.financulator.viewmodel.CoinListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CoinListFragment : BaseFragment<FragmentCoinListBinding>(FragmentCoinListBinding::inflate) {

    @Inject
    lateinit var coinListAdapter: CoinListAdapter
    private val viewModel: CoinListViewModel by viewModels()

    override fun observeViewModel() {
        with(viewModel) {
            coinListLoadState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Loading -> {
                        displayProgressBar(true)
                    }
                    is UiState.Success -> {
                        displayProgressBar(false)
                        coinListAdapter.coinList = state.data
                        coinListAdapter.notifyDataSetChanged()
                    }
                    is UiState.Error -> {
                        displayProgressBar(false)
                        displayError(state.error.message)
                    }
                }
            }

            filteredCoinList.observe(viewLifecycleOwner) {
                coinListAdapter.coinList = it
                coinListAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun setUpAdapters() {
        coinListAdapter.onItemClickListener = {
            val bundle = Bundle()
            bundle.putString(ConstVal.ID, it)
            findNavController().navigate(R.id.action_coinListFragment_to_purchaseFragment, bundle)
        }

        with(binding.coinListRecyclerViewCoinList) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = coinListAdapter
        }
    }

    override fun observeClicks() {
        with(binding) {
            toolbarCoinList.btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            toolbarCoinList.btnSearch.setOnClickListener {
                coinSearchViewCoinList.isGone = !coinSearchViewCoinList.isGone
            }

            coinSearchViewCoinList.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean = false
                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.filter(newText)
                    return false
                }
            })
        }
    }
}