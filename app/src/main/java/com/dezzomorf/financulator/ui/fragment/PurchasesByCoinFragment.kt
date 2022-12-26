package com.dezzomorf.financulator.ui.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.FragmentPurchasesByCoinBinding
import com.dezzomorf.financulator.extensions.showToast
import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.util.ConstVal
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.PurchaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchasesByCoinFragment : BaseFragment<FragmentPurchasesByCoinBinding>(FragmentPurchasesByCoinBinding::inflate) {

    private val viewModel: PurchaseViewModel by viewModels()
    lateinit var coin: Coin

    override fun onResume() {
        super.onResume()
        loadCurrentCoinData()
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

    private fun setDataToUi(coin: Coin) {
        binding.toolbarPurchasesByCoin.titleTextViewToolbar.text = getString(R.string.coin_info, coin.name, coin.symbol)
        //TODO
    }
}