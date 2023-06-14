package com.dezzomorf.financulator.ui.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.FragmentForgotPasswordBinding
import com.dezzomorf.financulator.extensions.isValidEmail
import com.dezzomorf.financulator.extensions.showToast
import com.dezzomorf.financulator.ui.activity.AuthorizationActivity
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.ForgotPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate) {

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun observeClicks() {
        binding.sendButtonForgotPassword.setOnClickListener {
            if (checkIsValidEmail()) viewModel.sendResetPasswordMessage(binding.emailEditTextForgotPassword.text.toString())
        }
        binding.backButtonForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_forgotPasswordFragment_to_signInFragment)
        }
    }

    override fun displayProgressBar(isDisplayed: Boolean) {
        (requireActivity() as AuthorizationActivity).displayProgressBar(isDisplayed)
    }

    override fun observeViewModel() {
        viewModel.sendResetPasswordMessageState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    displayProgressBar(true)
                }
                is UiState.Success -> {
                    displayProgressBar(false)
                    requireContext().showToast(getString(R.string.email_sent))
                    findNavController().navigate(R.id.action_forgotPasswordFragment_to_signInFragment)
                }
                is UiState.Error -> {
                    displayProgressBar(false)
                    requireContext().showToast(getString(R.string.network_error_default))
                }
            }
        }
    }

    private fun checkIsValidEmail(): Boolean {
        val email = binding.emailEditTextForgotPassword.text
        val result = email.isValidEmail()
        binding.emailEditTextForgotPassword.setErrorTextVisibility(!result)
        return result
    }
}