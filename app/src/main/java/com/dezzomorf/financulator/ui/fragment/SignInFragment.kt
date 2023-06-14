package com.dezzomorf.financulator.ui.fragment

import android.content.Intent
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.FragmentSignInBinding
import com.dezzomorf.financulator.extensions.*
import com.dezzomorf.financulator.ui.activity.AuthorizationActivity
import com.dezzomorf.financulator.ui.activity.SplashActivity
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(FragmentSignInBinding::inflate) {

    private val viewModel: SignInViewModel by viewModels()
    private var isPasswordShow = false

    override fun observeClicks() {
        binding.signUpTextViewSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        binding.loginButtonSignIn.setOnClickListener {
            val email = binding.emailEditTextSignIn.text.toString()
            val password = binding.passwordEditTextSignIn.text.toString()
            if (isDataValid()) viewModel.signInWithEmailAndPassword(email, password)
            requireContext().hideKeyboard(it)
        }
        binding.showPasswordButtonSignIn.setOnClickListener {
            isPasswordShow = !isPasswordShow
            updateViewsByPasswordShowState()
        }
        binding.forgotPasswordTextViewSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
        }
    }

    override fun observeViewModel() {
        viewModel.signInState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    displayProgressBar(true)
                }
                is UiState.Success -> {
                    displayProgressBar(false)
                    successSignIn()
                }
                is UiState.Error -> {
                    displayProgressBar(false)
                    requireContext().showToast(state.error.message ?: getString(R.string.network_error_default))
                }
            }
        }
    }

    override fun displayProgressBar(isDisplayed: Boolean) {
        (requireActivity() as AuthorizationActivity).displayProgressBar(isDisplayed)
    }

    private fun successSignIn() {
        requireContext().showToast(getString(R.string.success))
        if (viewModel.auth.currentUser?.isEmailVerified == true) {
            val intent = Intent(requireContext(), SplashActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else {
            findNavController().navigate(R.id.emailVerificationFragment)
        }
    }

    private fun isDataValid(): Boolean {
        val isEmailValid = checkIsValidEmail()
        val isPasswordValid = checkIsValidPassword()
        return isEmailValid && isPasswordValid
    }

    private fun checkIsValidEmail(): Boolean {
        val email = binding.emailEditTextSignIn.text
        val result = email.isValidEmail()
        binding.emailEditTextSignIn.setErrorTextVisibility(!result)
        return result
    }

    private fun checkIsValidPassword(): Boolean {
        val password = binding.passwordEditTextSignIn.text
        val result = password.isValidPassword()
        binding.passwordEditTextSignIn.setErrorTextVisibility(!result)
        return result
    }

    private fun updateViewsByPasswordShowState() {
        when (isPasswordShow) {
            true -> {
                binding.passwordEditTextSignIn.setTransformationMethod(null)
                binding.showPasswordImageButtonSignIn.horizontalSqueezeAnimation {
                    binding.showPasswordImageButtonSignIn.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                }
            }
            false -> {
                binding.passwordEditTextSignIn.setTransformationMethod(PasswordTransformationMethod())
                binding.showPasswordImageButtonSignIn.horizontalSqueezeAnimation {
                    binding.showPasswordImageButtonSignIn.setImageResource(R.drawable.ic_baseline_visibility_24)
                }
            }
        }
    }
}