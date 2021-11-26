package com.lampa.financulator.ui.fragment

import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lampa.financulator.R
import com.lampa.financulator.adapter.CoinListAdapter
import com.lampa.financulator.databinding.FragmentCoinListBinding
import com.lampa.financulator.ui.fragment.base.BaseFragment
import com.lampa.financulator.util.UiState
import com.lampa.financulator.viewmodel.CoinListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CoinListFragment : BaseFragment<FragmentCoinListBinding>() {
    override val layoutResId: Int = R.layout.fragment_coin_list

    @Inject
    lateinit var coinListAdapter: CoinListAdapter
    private val viewModel: CoinListViewModel by viewModels()

    override fun observeViewModel() {
        viewModel.getCoinList()
        viewModel.getCoinListState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    displayProgressBar(true)
                }
                is UiState.Success -> {
                    displayProgressBar(false)
                    state.data?.let { data ->
                        coinListAdapter.coinList = (data)
                        coinListAdapter.notifyItemRangeInserted(0, data.size)
                    }
                }
                is UiState.Error -> {
                    displayProgressBar(false)
                    displayError(state.error.message)
                }
            }
        }
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        binding.progressBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun displayError(message: String?) {
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, resources.getString(R.string.default_error_message), Toast.LENGTH_LONG).show()
        }
    }

    override fun setupAdapters() {
        with(binding.coinListRv) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = coinListAdapter
        }

        binding.coinSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                coinListAdapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun observeClicks() {
        with(binding) {
            toolbar.btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            toolbar.btnSearch.setOnClickListener {
                coinSearch.isGone = !coinSearch.isGone
            }
        }
    }
}