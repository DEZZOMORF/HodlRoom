package com.dezzomorf.financulator.ui.fragment

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.api.entity.CurrencyEntity
import com.dezzomorf.financulator.databinding.FragmentPurchaseBinding
import com.dezzomorf.financulator.extensions.*
import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.util.ConstVal
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.PurchaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.full.memberProperties

@AndroidEntryPoint
class PurchaseFragment : BaseFragment<FragmentPurchaseBinding>(FragmentPurchaseBinding::inflate) {

    private val viewModel: PurchaseViewModel by viewModels()

    override fun setUpUI() {
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
        binding.toolbarPurchase.buttonBackImageViewToolbar.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setDataToUi(coin: Coin) {
        binding.toolbarPurchase.titleTextViewToolbar.text = getString(R.string.coin_info, coin.name, coin.symbol)
        binding.coinLogoLeftImageViewPurchase.loadAndSetImage(coin.image)
        binding.coinLogoRightImageViewPurchase.loadAndSetImage(coin.image)

        binding.quantityEditTextPurchase.addTextChangedListener { updateUI() }
        binding.priceEditTextPurchase.addTextChangedListener { updateUI() }

        binding.spinnerUnitPurchase.setUpSpinner(CurrencyEntity::class.memberProperties.map { it.name }) { position ->
            binding.priceEditTextPurchase.setText(
                coin.currentPrice?.getPriceByPosition(position).formatPrice()
            )
            updateUI()
        }
        binding.spinnerUnitPurchase.selectSpinnerValue(ConstVal.defaultCurrency)

        binding.currentPriceTextViewPurchase.text = getString(
            R.string.currentPrice,
            coin.symbol,
            coin.currentPrice?.USD.formatPrice(),
            coin.currentPrice?.BTC.formatPrice()
        )

        viewsVisibility(true)
    }

    private fun viewsVisibility(isVisible: Boolean) {
        with(binding) {
            descriptionEditTextPurchase.isVisible = isVisible
            spinnerUnitPurchase.isVisible = isVisible
            quantityEditTextPurchase.isVisible = isVisible
            priceEditTextPurchase.isVisible = isVisible
            sumTextViewPurchase.isVisible = isVisible
            saveButtonPurchase.isVisible = isVisible
            separateLineView.isVisible = isVisible
        }
    }

    private fun loadCurrentCoinData() {
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

    private fun updateButton() {
        val quantity = binding.quantityEditTextPurchase.text.stringToFloatOrZero()
        val price = binding.priceEditTextPurchase.text.stringToFloatOrZero()
        binding.saveButtonPurchase.isEnabled = !(quantity == 0f || price == 0f)
    }

    private fun updateUI() {
        updateTotalCost()
        updateButton()
    }
}