package com.dezzomorf.financulator.ui.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.FragmentForgotPasswordBinding
import com.dezzomorf.financulator.extensions.isValidEmail
import com.dezzomorf.financulator.extensions.showToast
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.viewmodel.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate) {

    private val viewModel: BaseViewModel by viewModels()

    override fun observeClicks() {
        binding.sendButtonForgotPassword.setOnClickListener {
            if (checkIsValidEmail()) sendResetPasswordMessage(binding.emailEditTextForgotPassword.text.toString())
        }
        binding.backButtonForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_forgotPasswordFragment_to_signInFragment)
        }
    }

    private fun sendResetPasswordMessage(email: String) {
        displayAuthorizationActivityProgressBar(true)
        viewModel.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    requireContext().showToast(getString(R.string.email_sent))
                    findNavController().navigate(R.id.action_forgotPasswordFragment_to_signInFragment)
                } else {
                    requireContext().showToast(getString(R.string.network_error_default))
                }
                displayAuthorizationActivityProgressBar(false)
            }
    }

    private fun checkIsValidEmail(): Boolean {
        val email = binding.emailEditTextForgotPassword.text
        val result = email.isValidEmail()
        binding.emailEditTextForgotPassword.setErrorTextVisibility(!result)
        return result
    }
}