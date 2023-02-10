package com.dezzomorf.financulator.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.adapter.PurchasesRecyclerViewAdapter
import com.dezzomorf.financulator.databinding.FragmentPurchasesByCoinBinding
import com.dezzomorf.financulator.extensions.*
import com.dezzomorf.financulator.model.*
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.ui.view.FinanculatorDialog
import com.dezzomorf.financulator.util.ConstVal
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.MainViewModel
import com.dezzomorf.financulator.viewmodel.PurchaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PurchasesByCoinFragment : BaseFragment<FragmentPurchasesByCoinBinding>(FragmentPurchasesByCoinBinding::inflate) {

    @Inject
    lateinit var purchasesRecyclerViewAdapter: PurchasesRecyclerViewAdapter

    private val purchaseViewModel: PurchaseViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    lateinit var coin: Coin

    override fun onResume() {
        super.onResume()
        loadCurrentCoinData()
        purchaseViewModel.tryToGetPurchasesFromCache()
    }

    override fun setUpSwipeToRefresh() {
        binding.swipeMainPurchasesByCoin.setOnRefreshListener {
            purchaseViewModel.tryToGetPurchasesFromDataBase()
        }
    }

    override fun observeViewModel() {
        purchaseViewModel.coinState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    displayMainActivityProgressBar(true)
                }
                is UiState.Success -> {
                    displayMainActivityProgressBar(false)
                    setDataToUi(state.data)
                }
                is UiState.Error -> {
                    displayMainActivityProgressBar(false)
                    requireContext().showToast(state.error.message ?: getString(R.string.network_error_default))
                }
            }
        }

        mainViewModel.changesByCoinState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    displayMainActivityProgressBar(true)
                }
                is UiState.Success -> {
                    displayMainActivityProgressBar(false)
                    setUpTotalProfit(mainViewModel.profitSummary(state.data))
                }
                is UiState.Error -> {
                    displayMainActivityProgressBar(false)
                    requireContext().showToast(state.error.message ?: getString(R.string.network_error_default))
                }
            }
        }

        purchaseViewModel.getPurchasesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    displayMainActivityProgressBar(true)
                }
                is UiState.Success -> {
                    displayMainActivityProgressBar(false)
                    binding.swipeMainPurchasesByCoin.isRefreshing = false
                    val purchaseOfCurrentCoin = state.data.filter { it.coinId == coin.id }
                    if (purchaseOfCurrentCoin.isEmpty()) {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                        return@observe
                    }
                    purchasesRecyclerViewAdapter.setListWithAnimation(formatDataToChangesByPurchase(purchaseOfCurrentCoin))
                    mainViewModel.summaryChangesByCoins(purchaseOfCurrentCoin)
                }
                is UiState.Error -> {
                    displayMainActivityProgressBar(false)
                    requireContext().showToast(state.error.message ?: getString(R.string.network_error_default))
                }
            }
        }

        purchaseViewModel.deletePurchasesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    displayMainActivityProgressBar(true)
                }
                is UiState.Success -> {
                    displayMainActivityProgressBar(false)
                    purchaseViewModel.tryToGetPurchasesFromDataBase()
                }
                is UiState.Error -> {
                    displayMainActivityProgressBar(false)
                    requireContext().showToast(state.error.message ?: getString(R.string.network_error_default))
                }
            }
        }
    }

    private fun formatDataToChangesByPurchase(purchases: List<Purchase>): List<ChangesByPurchase> {
        return purchases.sortedBy { it.date }.map {
            ChangesByPurchase(
                purchaseId = it.purchaseId,
                description = it.description ?: "",
                currency = it.currency.name,
                price = it.price,
                quantity = it.quantity,
                sum = it.sum(),
                profitInPercents = it.profitInPercents(coin),
                profitInDollars = it.profitInDollars(coin),
            )
        }
    }

    override fun observeClicks() {
        binding.toolbarPurchasesByCoin.buttonBackImageViewToolbar.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbarPurchasesByCoin.addNewImageViewToolbar.setOnClickListener {
            if (this::coin.isInitialized) {
                val bundle = Bundle()
                bundle.putSerializable(ConstVal.ID, coin)
                findNavController().navigate(R.id.action_purchasesByCoinFragment_to_purchaseFragment, bundle)
            }
        }
    }

    private fun loadCurrentCoinData() {
        val argument = requireArguments().serializable<Coin>(ConstVal.ID)
        if (argument != null) {
            coin = argument
            purchaseViewModel.getCoinById(coin.id)
        } else {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun setUpAdapters() {
        purchasesRecyclerViewAdapter.onItemClick = {
            FinanculatorDialog(
                context = requireContext(),
                content = listOf(
                    FinanculatorDialog.FinanculatorDialogItem(
                        title = requireContext().resourcesCompat.getString(R.string.delete),
                        action = {
                            purchaseViewModel.deletePurchase(it.purchaseId)
                        }
                    ),
                    FinanculatorDialog.FinanculatorDialogItem(
                        title = requireContext().resourcesCompat.getString(android.R.string.cancel),
                        action = {}
                    )
                )
            ).createAndShow()
        }

        with(binding.purchaseListRecyclerViewPurchases) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = purchasesRecyclerViewAdapter
        }
    }

    private fun setDataToUi(coin: Coin) {
        binding.toolbarPurchasesByCoin.titleTextViewToolbar.text =
            getString(R.string.coin_info_with_price, coin.name, coin.symbol, coin.currentPrice[CurrencyName.USD.value].format())
    }

    private fun setUpTotalProfit(totalProfit: TotalProfit) {
        binding.totalProfitSumPurchasesByCoin.setTextColor(
            when {
                totalProfit.profitInPercents < 0f -> Color.RED
                else -> Color.GREEN
            }
        )

        binding.totalProfitPurchasesByCoin.isVisible = true
        binding.totalProfitSumPurchasesByCoin.text = totalProfit.sum.formatToTwoDigits() + "$"
        binding.totalProfitPercentPurchasesByCoin.text = totalProfit.profitInPercents.formatToTwoDigits() + "%"
        binding.totalProfitDollarPurchasesByCoin.text = totalProfit.profitInDollars.formatToTwoDigits() + "$"
    }
}