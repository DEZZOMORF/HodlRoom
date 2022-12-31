package com.dezzomorf.financulator.ui.fragment

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.adapter.MainRecyclerViewAdapter
import com.dezzomorf.financulator.databinding.FragmentMainBinding
import com.dezzomorf.financulator.extensions.formatToTwoDigits
import com.dezzomorf.financulator.extensions.resourcesCompat
import com.dezzomorf.financulator.extensions.showToast
import com.dezzomorf.financulator.model.TotalProfit
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.ui.view.FinanculatorDialog
import com.dezzomorf.financulator.util.ConstVal
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var mainRecyclerViewAdapter: MainRecyclerViewAdapter

    override fun setUpUI() {
        viewModel.tryToGetPurchasesFromDataBase()
    }

    override fun setUpSwipeToRefresh() {
        binding.swipeMain.setOnRefreshListener {
            viewModel.tryToGetPurchasesFromDataBase()
        }
    }

    override fun observeClicks() {
        binding.toolbarMain.buttonAddImageViewMainToolbar.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_coinListFragment)
        }
        binding.toolbarMain.buttonSettingsImageViewMainToolbar.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
        }
    }

    override fun observeViewModel() {
        viewModel.getPurchasesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    displayMainActivityProgressBar(true)
                }
                is UiState.Success -> {
                    displayMainActivityProgressBar(false)
                    binding.swipeMain.isRefreshing = false
                    viewModel.summaryChangesByCoins(state.data)
                }
                is UiState.Error -> {
                    displayMainActivityProgressBar(false)
                    requireContext().showToast(state.error.message ?: getString(R.string.network_error_default))
                }
            }
        }

        viewModel.changesByCoinState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    displayMainActivityProgressBar(true)
                }
                is UiState.Success -> {
                    displayMainActivityProgressBar(false)
                    mainRecyclerViewAdapter.setListWithAnimation(state.data)
                    setUpTotalProfit(viewModel.profitSummary(state.data))
                }
                is UiState.Error -> {
                    displayMainActivityProgressBar(false)
                    requireContext().showToast(state.error.message ?: getString(R.string.network_error_default))
                }
            }
        }

        viewModel.deletePurchasesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    displayMainActivityProgressBar(true)
                }
                is UiState.Success -> {
                    displayMainActivityProgressBar(false)
                    viewModel.tryToGetPurchasesFromDataBase()
                }
                is UiState.Error -> {
                    displayMainActivityProgressBar(false)
                    requireContext().showToast(state.error.message ?: getString(R.string.network_error_default))
                }
            }
        }
    }

    override fun setUpAdapters() {
        mainRecyclerViewAdapter.onItemClick = {
            val bundle = Bundle()
            bundle.putSerializable(ConstVal.ID, it.coin)
            findNavController().navigate(R.id.purchasesByCoinFragment, bundle)
        }

        mainRecyclerViewAdapter.onItemLongClick = {
            FinanculatorDialog(
                context = requireContext(),
                content = listOf(
                    FinanculatorDialog.FinanculatorDialogItem(
                        title = requireContext().resourcesCompat.getString(R.string.delete),
                        action = {
                            viewModel.auth.currentUser?.let { user ->
                                viewModel.deletePurchasesByCoin(it.coin, user.uid)
                            }
                        }
                    ),
                    FinanculatorDialog.FinanculatorDialogItem(
                        title = requireContext().resourcesCompat.getString(android.R.string.cancel),
                        action = {}
                    )
                )
            ).createAndShow()
        }

        with(binding.purchaseListRecyclerViewMain) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mainRecyclerViewAdapter
        }
    }

    private fun setUpTotalProfit(totalProfit: TotalProfit) {
        binding.totalProfitMain.isVisible = true
        binding.totalProfitSumMain.text = totalProfit.sum.formatToTwoDigits() + "$"
        binding.totalProfitPercentMain.text = totalProfit.profitInPercents.formatToTwoDigits() + "%"
        binding.totalProfitDollarMain.text = totalProfit.profitInDollars.formatToTwoDigits() + "$"
    }
}