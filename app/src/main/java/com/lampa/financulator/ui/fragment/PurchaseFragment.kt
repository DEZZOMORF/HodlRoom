package com.lampa.financulator.ui.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lampa.financulator.R
import com.lampa.financulator.databinding.FragmentPurchaseBinding
import com.lampa.financulator.extensions.loadAndSetImage
import com.lampa.financulator.ui.fragment.base.BaseFragment
import com.lampa.financulator.util.ConstVal.ID
import com.lampa.financulator.util.UiState
import com.lampa.financulator.viewmodel.PurchaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchaseFragment : BaseFragment<FragmentPurchaseBinding>(FragmentPurchaseBinding::inflate) {
    private val viewModel: PurchaseViewModel by viewModels()

    override fun observeViewModel() {
        requireArguments().getString(ID)?.let {
            viewModel.getCoinById(it)
        }

        viewModel.coinState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    displayProgressBar(true)
                }
                is UiState.Success -> {
                    displayProgressBar(false)
                    with(state.data) {
                        binding.toolbar.coinInfoTextViewToolbar.text = getString(R.string.coin_info, name, symbol)
                        binding.coinLogoLeftImageViewPurchase.loadAndSetImage(image)
                        binding.coinLogoRightImageViewPurchase.loadAndSetImage(image)
                        binding.currentPriceTextViewPurchase.text = getString(
                            R.string.currentPrice,
                            symbol,
                            viewModel.formatPrice(currentPrice?.USD),
                            viewModel.formatPrice(currentPrice?.BTC)
                        )
                    }
                }
                is UiState.Error -> {
                    displayProgressBar(false)
                    displayError(state.error.message)
                }
            }
        }
    }

    override fun observeClicks() {
        binding.toolbar.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}