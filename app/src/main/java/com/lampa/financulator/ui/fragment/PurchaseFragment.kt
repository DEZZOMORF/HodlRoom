package com.lampa.financulator.ui.fragment

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lampa.financulator.R
import com.lampa.financulator.databinding.FragmentPurchaseBinding
import com.lampa.financulator.ui.fragment.base.BaseFragment
import com.lampa.financulator.util.ConstVal.ID
import com.lampa.financulator.util.Logger
import com.lampa.financulator.util.UiState
import com.lampa.financulator.viewmodel.PurchaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchaseFragment : BaseFragment<FragmentPurchaseBinding>() {
    override val layoutResId: Int = R.layout.fragment_purchase

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
                    binding.coin = state.data
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

    override fun observeClicks() {
        binding.toolbar.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}