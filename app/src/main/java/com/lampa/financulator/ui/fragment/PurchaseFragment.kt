package com.lampa.financulator.ui.fragment

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lampa.financulator.R
import com.lampa.financulator.api.entity.CurrencyEntity
import com.lampa.financulator.databinding.FragmentPurchaseBinding
import com.lampa.financulator.extensions.loadAndSetImage
import com.lampa.financulator.extensions.selectSpinnerValue
import com.lampa.financulator.extensions.setList
import com.lampa.financulator.model.Coin
import com.lampa.financulator.ui.fragment.base.BaseFragment
import com.lampa.financulator.util.ConstVal
import com.lampa.financulator.util.UiState
import com.lampa.financulator.viewmodel.PurchaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.full.memberProperties


@AndroidEntryPoint
class PurchaseFragment : BaseFragment<FragmentPurchaseBinding>(FragmentPurchaseBinding::inflate) {

    private val viewModel: PurchaseViewModel by viewModels()

    override fun setUpUi() {
        loadCoinList()
    }

    override fun observeViewModel() {
        viewModel.coinState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    displayProgressBar(true)
                }
                is UiState.Success -> {
                    displayProgressBar(false)
                    setDataToUi(state.data)
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

    private fun setDataToUi(coin: Coin) {
        binding.toolbar.coinInfoTextViewToolbar.text = getString(R.string.coin_info, coin.name, coin.symbol)
        binding.coinLogoLeftImageViewPurchase.loadAndSetImage(coin.image)
        binding.coinLogoRightImageViewPurchase.loadAndSetImage(coin.image)
        binding.spinner.setList(CurrencyEntity::class.memberProperties.map { it.name })
        binding.spinner.selectSpinnerValue(ConstVal.defaultCurrency)
        binding.currentPriceTextViewPurchase.text = getString(
            R.string.currentPrice,
            coin.symbol,
            viewModel.formatPrice(coin.currentPrice?.USD),
            viewModel.formatPrice(coin.currentPrice?.BTC)
        )

        makeViewsVisible()
    }

    private fun makeViewsVisible() {
        with(binding) {
            description.isVisible = true
            spinner.isVisible = true
            amount.isVisible = true
            price.isVisible = true
            sum.isVisible = true
            btnSave.isVisible = true
            view.isVisible = true
        }
    }

    private fun loadCoinList() {
        requireArguments().getString(ConstVal.ID)?.let {
            viewModel.getCoinById(it)
        }
    }
}