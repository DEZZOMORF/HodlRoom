package com.dezzomorf.financulator.ui.fragment

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.api.entity.PurchaseEntity
import com.dezzomorf.financulator.databinding.FragmentPurchaseBinding
import com.dezzomorf.financulator.extensions.*
import com.dezzomorf.financulator.model.Coin
import com.dezzomorf.financulator.model.CurrencyName
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.util.ConstVal
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.PurchaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class PurchaseFragment : BaseFragment<FragmentPurchaseBinding>(FragmentPurchaseBinding::inflate) {

    private val viewModel: PurchaseViewModel by viewModels()
    lateinit var coinId: String

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

        viewModel.addPurchaseState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    displayMainActivityProgressBar(true)
                }
                is UiState.Success -> {
                    displayMainActivityProgressBar(false)
                    requireContext().showToast(getString(R.string.success))
                    findNavController().popBackStack()
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
        binding.saveButtonPurchase.setOnClickListener {
            if (binding.saveButtonPurchase.isEnabled) {
                val purchase = getPurchaseData()
                viewModel.addPurchase(purchase)
            }
        }
    }

    private fun setDataToUi(coin: Coin) {
        binding.toolbarPurchase.titleTextViewToolbar.text = getString(R.string.coin_info, coin.name, coin.symbol)
        binding.coinLogoLeftImageViewPurchase.loadAndSetImage(coin.logo)
        binding.coinLogoRightImageViewPurchase.loadAndSetImage(coin.logo)

        binding.quantityEditTextPurchase.addTextChangedListener { updateUI() }
        binding.priceEditTextPurchase.addTextChangedListener { updateUI() }

        setUpSpinner(coin)

        binding.currentPriceTextViewPurchase.text = getString(
            R.string.currentPrice,
            coin.symbol,
            coin.currentPrice[CurrencyName.USD.value].format(),
            coin.currentPrice[CurrencyName.BTC.value].format()
        )

        viewsVisibility(true)
    }

    private fun setUpSpinner(coin: Coin) {
        val currencyList: MutableList<String> = mutableListOf()
        for (currency in coin.currentPrice.keys) {
            currencyList.add(
                currency.uppercase(Locale.getDefault())
            )
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.layout_spinner, R.id.spinner_text_view_spinner_item, currencyList)
        adapter.setDropDownViewResource(R.layout.layout_spinner_item)
        binding.spinnerUnitPurchase.adapter = adapter
        binding.spinnerUnitPurchase.selectSpinnerValue(CurrencyName.USD.name)

        binding.spinnerUnitPurchase.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val currentPriceByCurrency = coin.currentPrice[parent.getItemAtPosition(position).toString().lowercase(Locale.getDefault())]
                binding.priceEditTextPurchase.setText(currentPriceByCurrency.format())
                updateUI()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
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
        val argument = requireArguments().getString(ConstVal.ID)
        if (argument != null) {
            coinId = argument
            viewModel.getCoinById(argument)
        } else {
            requireActivity().onBackPressed()
        }
    }

    private fun updateTotalCost() {
        val quantity = binding.quantityEditTextPurchase.text.stringToFloatOrZero()
        val price = binding.priceEditTextPurchase.text.stringToFloatOrZero()
        val unit = binding.spinnerUnitPurchase.selectedItem as String
        val sum = quantity * price
        binding.sumTextViewPurchase.text = getString(R.string.total_cost, sum.format(), unit)
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

    private fun getPurchaseData(): PurchaseEntity {
        return PurchaseEntity(
            coinId = coinId,
            currency = binding.spinnerUnitPurchase.selectedItem as String,
            description = binding.descriptionEditTextPurchase.text.toString(),
            price = binding.priceEditTextPurchase.text.stringToFloatOrZero(),
            quantity = binding.quantityEditTextPurchase.text.stringToFloatOrZero()
        )
    }
}