package com.dezzomorf.financulator.ui.fragment

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.FragmentSignInBinding
import com.dezzomorf.financulator.extensions.hideKeyboard
import com.dezzomorf.financulator.extensions.isValidEmail
import com.dezzomorf.financulator.extensions.isValidPassword
import com.dezzomorf.financulator.ui.activity.SplashActivity
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.viewmodel.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(FragmentSignInBinding::inflate) {

    private val viewModel: BaseViewModel by viewModels()

    override fun setUpUi() {
        setUpEditTextListeners()
    }

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
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        displayAuthorizationActivityProgressBar(true)
        viewModel.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    displayToast(getString(R.string.success))
                    val intent = Intent(requireContext(), SplashActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    displayToast(task.exception?.message)
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
        binding.invalidEmailTextViewSignIn.visibility = getVisibilityByValidation(!result)
        return result
    }

    private fun checkIsValidPassword(): Boolean {
        val password = binding.passwordEditTextSignIn.text
        val result = password.isValidPassword()
        binding.invalidPasswordTextViewSignIn.visibility = getVisibilityByValidation(!result)
        return result
    }

    private fun getVisibilityByValidation(isValidate: Boolean): Int {
        return when (isValidate) {
            true -> View.VISIBLE
            false -> View.INVISIBLE
        }
    }

    private fun setUpEditTextListeners() {
        binding.emailEditTextSignIn.addTextChangedListener {
            binding.invalidEmailTextViewSignIn.visibility = View.INVISIBLE
        }
        binding.passwordEditTextSignIn.addTextChangedListener {
            binding.invalidPasswordTextViewSignIn.visibility = View.INVISIBLE
        }
    }
}