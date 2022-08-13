package com.dezzomorf.financulator.ui.fragment

import android.content.Intent
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.FragmentSignUpBinding
import com.dezzomorf.financulator.extensions.hideKeyboard
import com.dezzomorf.financulator.extensions.isValidEmail
import com.dezzomorf.financulator.extensions.isValidPassword
import com.dezzomorf.financulator.ui.activity.SplashActivity
import com.dezzomorf.financulator.ui.activity.intent.MainActivityIntent
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    private val viewModel: SignUpViewModel by viewModels()

    override fun observeClicks() {
        binding.signInTextViewSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        binding.signUpButtonSignUp.setOnClickListener {
            val email = binding.emailEditTextSignUp.text.toString()
            val password = binding.passwordEditTextSignUp.text.toString()
            if (isDataValid()) {
                displayAuthorizationActivityProgressBar(true)
                createUserWithEmailAndPassword(email, password)
            } else {
                displayToast(getString(R.string.invalid_data))
            }
            requireContext().hideKeyboard(it)
        }
    }

    private fun createUserWithEmailAndPassword(email: String, password: String) {
        viewModel.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    displayToast(getString(R.string.success))
                    val intent = Intent(requireContext(), SplashActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    displayToast(getString(R.string.authentication_failed))
                }
                displayAuthorizationActivityProgressBar(false)
            }
    }

    private fun isDataValid(): Boolean {
        val email = binding.emailEditTextSignUp.text
        val password = binding.passwordEditTextSignUp.text
        val confirmPassword = binding.confirmPasswordEditTextSignUp.text
        return email.isValidEmail() && password.isValidPassword() && password.toString() == confirmPassword.toString()
    }
}