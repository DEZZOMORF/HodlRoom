package com.dezzomorf.financulator.ui.fragment

import android.content.Intent
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.FragmentEmailVerificationBinding
import com.dezzomorf.financulator.extensions.resourcesCompat
import com.dezzomorf.financulator.extensions.showToast
import com.dezzomorf.financulator.ui.activity.SplashActivity
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.util.UiState
import com.dezzomorf.financulator.viewmodel.EmailVerificationViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmailVerificationFragment : BaseFragment<FragmentEmailVerificationBinding>(FragmentEmailVerificationBinding::inflate) {

    private val viewModel: EmailVerificationViewModel by viewModels()
    private lateinit var timer: CountDownTimer

    override fun setUpUI() {
        viewModel.sendEmailVerification()
    }

    override fun observeClicks() {
        binding.verificationInfoTextViewEmailVerification.setOnClickListener {
            viewModel.sendEmailVerification()
        }
        binding.doneButtonEmailVerification.setOnClickListener {
            viewModel.checkIsEmailVerified()
        }
        binding.backButtonEmailVerification.setOnClickListener {
            findNavController().navigate(R.id.signInFragment)
        }
    }

    override fun observeViewModel() {
        viewModel.isEmailVerifiedState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    displayAuthorizationActivityProgressBar(true)
                }
                is UiState.Success -> {
                    displayAuthorizationActivityProgressBar(false)
                    successEmailVerification()
                }
                is UiState.Error -> {
                    displayAuthorizationActivityProgressBar(false)
                    requireContext().showToast(getString(R.string.please_verify_your_email))
                }
            }
        }

        viewModel.sendEmailVerificationState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    displayAuthorizationActivityProgressBar(true)
                }
                is UiState.Success -> {
                    displayAuthorizationActivityProgressBar(false)
                    val user = state.data
                    successEmailVerificationSent(user)
                }
                is UiState.Error -> {
                    displayAuthorizationActivityProgressBar(false)
                    errorEmailVerificationSent()
                }
            }
        }
    }

    private fun errorEmailVerificationSent() {
        binding.verificationMessageStateTextViewEmailVerification.text = getString(R.string.verification_email_err)
        binding.doneButtonEmailVerification.visibility = View.GONE
        binding.verificationInfoTextViewEmailVerification.visibility = View.GONE
        binding.backButtonEmailVerification.visibility = View.VISIBLE
    }

    private fun successEmailVerificationSent(user: FirebaseUser) {
        val title: Spannable = SpannableString(getString(R.string.verification_email_sent, user.email))
        title.setSpan(
            ForegroundColorSpan(requireContext().resourcesCompat.getColor(R.color.purple_200)),
            getString(R.string.verification_email_sent, "").length,
            getString(R.string.verification_email_sent, user.email).length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.verificationMessageStateTextViewEmailVerification.text = title
        binding.doneButtonEmailVerification.visibility = View.VISIBLE
        binding.verificationInfoTextViewEmailVerification.visibility = View.VISIBLE
        binding.backButtonEmailVerification.visibility = View.GONE
        startSendAgainTimer()
    }

    private fun successEmailVerification() {
        timer.cancel()
        val intent = Intent(requireActivity(), SplashActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun setUpVerifyInfoText(timer: Long? = null) {
        if (timer == null) {
            binding.verificationInfoTextViewEmailVerification.text = resources.getString(R.string.verification_email_info_text)
        } else {
            binding.verificationInfoTextViewEmailVerification.text =
                resources.getString(R.string.verification_email_info_text) + " ($timer)"
        }
    }

    private fun startSendAgainTimer() {
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                setUpVerifyInfoText(millisUntilFinished / 1000)
                binding.verificationInfoTextViewEmailVerification.isEnabled = false
                binding.verificationInfoTextViewEmailVerification.setTextColor(requireContext().resourcesCompat.getColor(R.color.teal_700))
            }

            override fun onFinish() {
                setUpVerifyInfoText()
                binding.verificationInfoTextViewEmailVerification.isEnabled = true
                binding.verificationInfoTextViewEmailVerification.setTextColor(requireContext().resourcesCompat.getColor(R.color.white))
            }
        }
        timer.start()
    }
}