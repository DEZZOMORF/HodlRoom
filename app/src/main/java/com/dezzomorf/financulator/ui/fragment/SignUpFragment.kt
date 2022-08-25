package com.dezzomorf.financulator.ui.fragment

import android.content.Intent
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.FragmentSignUpBinding
import com.dezzomorf.financulator.extensions.horizontalSqueezeAnimation
import com.dezzomorf.financulator.extensions.hideKeyboard
import com.dezzomorf.financulator.extensions.isValidEmail
import com.dezzomorf.financulator.extensions.isValidPassword
import com.dezzomorf.financulator.ui.activity.SplashActivity
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.viewmodel.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    private val viewModel: BaseViewModel by viewModels()
    private var isPasswordShow = false

    override fun observeClicks() {
        binding.signInTextViewSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        binding.signUpButtonSignUp.setOnClickListener {
            val email = binding.emailEditTextSignUp.text.toString()
            val password = binding.passwordEditTextSignUp.text.toString()
            if (isDataValid()) createUserWithEmailAndPassword(email, password)
            requireContext().hideKeyboard(it)
        }
        binding.showPasswordButtonSignUp.setOnClickListener {
            isPasswordShow = !isPasswordShow
            updateViewsByPasswordShowState()
        }
    }

    private fun createUserWithEmailAndPassword(email: String, password: String) {
        displayAuthorizationActivityProgressBar(true)
        viewModel.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    displayToast(getString(R.string.success))
                    findNavController().navigate(R.id.emailVerificationFragment)
                } else {
                    displayToast(task.exception?.message)
                }
                displayAuthorizationActivityProgressBar(false)
            }
    }

    private fun isDataValid(): Boolean {
        val isEmailValid = checkIsValidEmail()
        val isPasswordValid = checkIsValidPassword()
        val isPasswordMatch = checkIsPasswordMatch()
        return isEmailValid && isPasswordValid && isPasswordMatch
    }

    private fun checkIsValidEmail(): Boolean {
        val email = binding.emailEditTextSignUp.text
        val result = email.isValidEmail()
        binding.emailEditTextSignUp.setErrorTextVisibility(!result)
        return result
    }

    private fun checkIsValidPassword(): Boolean {
        val password = binding.passwordEditTextSignUp.text
        val result = password.isValidPassword()
        binding.passwordEditTextSignUp.setErrorTextVisibility(!result)
        return result
    }

    private fun checkIsPasswordMatch(): Boolean {
        val password = binding.passwordEditTextSignUp.text.toString()
        val confirmPassword = binding.confirmPasswordEditTextSignUp.text.toString()
        val result = password == confirmPassword
        binding.confirmPasswordEditTextSignUp.setErrorTextVisibility(!result)
        return result
    }

    private fun updateViewsByPasswordShowState() {
        when (isPasswordShow) {
            true -> {
                binding.passwordEditTextSignUp.setTransformationMethod(null)
                binding.confirmPasswordEditTextSignUp.setTransformationMethod(null)
                binding.showPasswordImageButtonSignUp.horizontalSqueezeAnimation {
                    binding.showPasswordImageButtonSignUp.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                }
            }
            false -> {
                binding.passwordEditTextSignUp.setTransformationMethod(PasswordTransformationMethod())
                binding.confirmPasswordEditTextSignUp.setTransformationMethod(PasswordTransformationMethod())
                binding.showPasswordImageButtonSignUp.horizontalSqueezeAnimation {
                    binding.showPasswordImageButtonSignUp.setImageResource(R.drawable.ic_baseline_visibility_24)
                }
            }
        }
    }
}