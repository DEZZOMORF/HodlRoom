package com.dezzomorf.financulator.ui.fragment

import android.content.Intent
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.FragmentSignInBinding
import com.dezzomorf.financulator.extensions.*
import com.dezzomorf.financulator.ui.activity.SplashActivity
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.viewmodel.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(FragmentSignInBinding::inflate) {

    private val viewModel: BaseViewModel by viewModels()
    private var isPasswordShow = false

    override fun observeClicks() {
        binding.signUpTextViewSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        binding.loginButtonSignIn.setOnClickListener {
            val email = binding.emailEditTextSignIn.text.toString()
            val password = binding.passwordEditTextSignIn.text.toString()
            if (isDataValid()) signInWithEmailAndPassword(email, password)
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

    private fun signInWithEmailAndPassword(email: String, password: String) {
        displayAuthorizationActivityProgressBar(true)
        viewModel.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    requireContext().showToast(getString(R.string.success))
                    if (viewModel.auth.currentUser?.isEmailVerified == true) {
                        val intent = Intent(requireContext(), SplashActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        findNavController().navigate(R.id.emailVerificationFragment)
                    }
                } else {
                    requireContext().showToast(task.exception?.message?: getString(R.string.network_error_default))
                }
                displayAuthorizationActivityProgressBar(false)
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