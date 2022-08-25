package com.dezzomorf.financulator.ui.fragment

import android.content.Intent
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.viewModels
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.FragmentEmailVerificationBinding
import com.dezzomorf.financulator.extensions.resourcesCompat
import com.dezzomorf.financulator.ui.activity.SplashActivity
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.viewmodel.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmailVerificationFragment : BaseFragment<FragmentEmailVerificationBinding>(FragmentEmailVerificationBinding::inflate) {

    private val viewModel: BaseViewModel by viewModels()
    private lateinit var timer: CountDownTimer

    override fun observeClicks() {
        binding.verificationInfoTextViewEmailVerification.setOnClickListener {
            verifyEmail()
        }
        binding.doneButtonEmailVerification.setOnClickListener {
            checkIsEmailVerified()
        }
    }

    override fun onViewCreatedActions() {
        verifyEmail()
    }

    private fun verifyEmail() {
        displayAuthorizationActivityProgressBar(true)
        val user = viewModel.auth.currentUser
        user?.let {
            it.sendEmailVerification().addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    binding.verificationMessageStateTextViewEmailVerification.text = getString(R.string.verification_email_sent, user.email)
                    startSendAgainTimer()
                    binding.doneButtonEmailVerification.visibility = View.VISIBLE
                    binding.verificationInfoTextViewEmailVerification.visibility = View.VISIBLE
                } else {
                    binding.verificationMessageStateTextViewEmailVerification.text = getString(R.string.verification_email_err)
                    binding.doneButtonEmailVerification.visibility = View.GONE
                    binding.verificationInfoTextViewEmailVerification.visibility = View.GONE
                }
                displayAuthorizationActivityProgressBar(false)
            }
        }
    }

    private fun setUpVerifyInfoText(timer: Long? = null) {
        if (timer == null) {
            binding.verificationInfoTextViewEmailVerification.text = resources.getString(R.string.verification_email_info_text)
        } else {
            binding.verificationInfoTextViewEmailVerification.text = resources.getString(R.string.verification_email_info_text) + " ($timer)"
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

    private fun checkIsEmailVerified(){
        viewModel.auth.currentUser?.let { user ->
            user.reload()
            if (user.isEmailVerified) {
                timer.cancel()
                val intent = Intent(requireActivity(), SplashActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                displayToast(getString(R.string.please_verify_your_email))
            }
        }
    }
}