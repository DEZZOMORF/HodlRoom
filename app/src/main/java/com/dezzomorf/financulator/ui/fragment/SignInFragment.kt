package com.dezzomorf.financulator.ui.fragment

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.FragmentMainBinding
import com.dezzomorf.financulator.databinding.FragmentSignInBinding
import com.dezzomorf.financulator.extensions.hideKeyboard
import com.dezzomorf.financulator.extensions.isValidEmail
import com.dezzomorf.financulator.extensions.isValidPassword
import com.dezzomorf.financulator.ui.activity.SplashActivity
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.viewmodel.MainViewModel
import com.dezzomorf.financulator.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(FragmentSignInBinding::inflate) {

    private val viewModel: SignInViewModel by viewModels()

    override fun observeClicks() {
        binding.signUpTextViewSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        binding.loginButtonSignIn.setOnClickListener {
            val email = binding.emailEditTextSignIn.text.toString()
            val password = binding.passwordEditTextSignIn.text.toString()
            if (isDataValid()) {
                displayAuthorizationActivityProgressBar(true)
                signInWithEmailAndPassword(email, password)
            } else {
                displayToast(getString(R.string.invalid_data))
            }
            requireContext().hideKeyboard(it)
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        viewModel.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    displayToast(getString(R.string.success))
                    val intent = Intent(requireContext(), SplashActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    displayToast(getString(R.string.authentication_failed))
                }
            }
    }

    private fun isDataValid(): Boolean {
        val email = binding.emailEditTextSignIn.text
        val password = binding.passwordEditTextSignIn.text
        return email.isValidEmail() && password.isValidPassword()
    }
}