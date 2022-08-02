package com.lampa.financulator.ui.fragment

import android.util.Log
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lampa.financulator.R
import com.lampa.financulator.api.entity.CurrencyEntity
import com.lampa.financulator.databinding.FragmentPurchaseBinding
import com.lampa.financulator.extensions.*
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
        binding.toolbarPurchase.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setDataToUi(coin: Coin) {
        binding.toolbarPurchase.coinInfoTextViewToolbar.text = getString(R.string.coin_info, coin.name, coin.symbol)
        binding.coinLogoLeftImageViewPurchase.loadAndSetImage(coin.image)
        binding.coinLogoRightImageViewPurchase.loadAndSetImage(coin.image)

        binding.quantityEditTextPurchase.addTextChangedListener { updateTotalCost() }
        binding.priceEditTextPurchase.addTextChangedListener { updateTotalCost() }

        binding.spinnerUnitPurchase.setUpSpinner(CurrencyEntity::class.memberProperties.map { it.name }) { position ->
            binding.priceEditTextPurchase.setText(
                    coin.currentPrice?.getPriceByPosition(position).formatPrice()
            )
            updateTotalCost()
        }
        binding.spinnerUnitPurchase.selectSpinnerValue(ConstVal.defaultCurrency)

        binding.currentPriceTextViewPurchase.text = getString(
            R.string.currentPrice,
            coin.symbol,
            coin.currentPrice?.USD.formatPrice(),
            coin.currentPrice?.BTC.formatPrice()
        )

        makeViewsVisible()
    }

    private fun makeViewsVisible() {
        with(binding) {
            descriptionEditTextPurchase.isVisible = true
            spinnerUnitPurchase.isVisible = true
            quantityEditTextPurchase.isVisible = true
            priceEditTextPurchase.isVisible = true
            sumTextViewPurchase.isVisible = true
            btnSavePurchase.isVisible = true
            separateLineView.isVisible = true
            descriptionHintEditTextPurchase.isVisible = true
            quantityHintEditTextPurchase.isVisible = true
            priceHintEditTextPurchase.isVisible = true
        }
    }

    private fun loadCoinList() {
        requireArguments().getString(ConstVal.ID)?.let {
            viewModel.getCoinById(it)
        }
    }

    private fun updateTotalCost() {
        val quantity = binding.quantityEditTextPurchase.text.stringToFloatOrZero()
        val price = binding.priceEditTextPurchase.text.stringToFloatOrZero()
        val unit = binding.spinnerUnitPurchase.selectedItem as String
        val sum = quantity * price
        binding.sumTextViewPurchase.text = getString(R.string.total_cost, sum.formatPrice(), unit)
    }
}