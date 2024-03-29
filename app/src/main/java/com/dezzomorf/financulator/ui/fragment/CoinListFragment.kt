package com.dezzomorf.financulator.ui.fragment

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.adapter.CoinListRecyclerViewAdapter
import com.dezzomorf.financulator.databinding.FragmentCoinListBinding
import com.dezzomorf.financulator.extensions.scaleInAnimation
import com.dezzomorf.financulator.extensions.showToast
import com.dezzomorf.financulator.ui.activity.MainActivity
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.util.ConstVal
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.CoinListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CoinListFragment : BaseFragment<FragmentCoinListBinding>(FragmentCoinListBinding::inflate) {

    @Inject
    lateinit var coinListAdapter: CoinListRecyclerViewAdapter
    private val viewModel: CoinListViewModel by viewModels()

    override fun setUpUI() {
        viewModel.getCoinList()
    }

    override fun observeViewModel() {
        with(viewModel) {
            coinListLoadState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Loading -> {
                        requireActivity()
                        displayProgressBar(true)
                    }
                    is UiState.Success -> {
                        displayProgressBar(false)
                        binding.toolbarCoinList.buttonSearchImageViewToolbarCoinList.scaleInAnimation()
                        coinListAdapter.setList(state.data)
                    }
                    is UiState.Error -> {
                        displayProgressBar(false)
                        requireContext().showToast(state.error.message.toString())
                    }
                }
            }

            filteredCoinList.observe(viewLifecycleOwner) { newList ->
                binding.nothingFoundTextViewCoinList.isVisible = newList.isEmpty()
                coinListAdapter.setListWithAnimation(newList)
            }
        }
    }

    override fun setUpAdapters() {
        coinListAdapter.onItemClick = {
            val bundle = Bundle()
            bundle.putSerializable(ConstVal.ID, it)
            findNavController().navigate(R.id.action_coinListFragment_to_purchaseFragment, bundle)
        }

        with(binding.coinListRecyclerViewCoinList) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = coinListAdapter
        }
    }

    override fun observeClicks() {
        with(binding) {
            toolbarCoinList.buttonBackImageViewToolbarCoinList.setOnClickListener {
                findNavController().popBackStack()
            }

            toolbarCoinList.buttonSearchImageViewToolbarCoinList.setOnClickListener {
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


    override fun displayProgressBar(isDisplayed: Boolean) {
        (requireActivity() as MainActivity).displayProgressBar(isDisplayed)
    }
}