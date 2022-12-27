package com.dezzomorf.financulator.ui.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.adapter.PurchasesRecyclerViewAdapter
import com.dezzomorf.financulator.databinding.FragmentPurchasesByCoinBinding
import com.dezzomorf.financulator.extensions.format
import com.dezzomorf.financulator.extensions.resourcesCompat
import com.dezzomorf.financulator.extensions.showToast
import com.dezzomorf.financulator.model.ChangesByPurchase
import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.model.CurrencyName
import com.dezzomorf.financulator.model.Purchase
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.ui.view.FinanculatorDialog
import com.dezzomorf.financulator.util.ConstVal
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.PurchaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PurchasesByCoinFragment : BaseFragment<FragmentPurchasesByCoinBinding>(FragmentPurchasesByCoinBinding::inflate) {

    @Inject
    lateinit var purchasesRecyclerViewAdapter: PurchasesRecyclerViewAdapter

    private val viewModel: PurchaseViewModel by viewModels()
    lateinit var coin: Coin

    override fun onResume() {
        super.onResume()
        loadCurrentCoinData()
    }

    override fun setUpUI() {
        viewModel.getPurchases()
    }

    override fun observeViewModel() {
        viewModel.coinState.observe(viewLifecycleOwner) { state ->
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

        viewModel.getPurchasesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    displayMainActivityProgressBar(true)
                }
                is UiState.Success -> {
                    displayMainActivityProgressBar(false)
                    val purchaseOfCurrentCoin = state.data.filter { it.coinId == coin.id }
                    purchasesRecyclerViewAdapter.setListWithAnimation(formatDataToChangesByPurchase(purchaseOfCurrentCoin))
                }
                is UiState.Error -> {
                    displayMainActivityProgressBar(false)
                    requireContext().showToast(state.error.message ?: getString(R.string.network_error_default))
                }
            }
        }
    }

    private fun formatDataToChangesByPurchase(purchases: List<Purchase>): List<ChangesByPurchase> {
        return purchases.map {
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
    }

    private fun loadCurrentCoinData() {
        val argument = requireArguments().getSerializable(ConstVal.ID)
        if (argument != null) {
            coin = argument as Coin
            viewModel.getCoinById(coin.id)
        } else {
            requireActivity().onBackPressed()
        }
    }


    override fun setUpAdapters() {
        purchasesRecyclerViewAdapter.onItemLongClick = {
            FinanculatorDialog(
                context = requireContext(),
                content = listOf(
                    FinanculatorDialog.FinanculatorDialogItem(
                        title = requireContext().resourcesCompat.getString(R.string.delete),
                        action = {
                            viewModel.auth.currentUser?.let { user ->
                                viewModel.deletePurchase(it.purchaseId, user.uid)
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

        with(binding.purchaseListRecyclerViewPurchases) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = purchasesRecyclerViewAdapter
        }
    }

    private fun setDataToUi(coin: Coin) {
        binding.toolbarPurchasesByCoin.titleTextViewToolbar.text = getString(R.string.coin_info_with_price, coin.name, coin.symbol, coin.currentPrice[CurrencyName.USD.value].format())
    }
}