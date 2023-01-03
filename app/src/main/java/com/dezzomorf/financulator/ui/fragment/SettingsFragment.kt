package com.dezzomorf.financulator.ui.fragment

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.FragmentSettingsBinding
import com.dezzomorf.financulator.extensions.resourcesCompat
import com.dezzomorf.financulator.extensions.showToast
import com.dezzomorf.financulator.manager.SettingsManager
import com.dezzomorf.financulator.ui.activity.SplashActivity
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.ui.view.FinanculatorDialog
import com.dezzomorf.financulator.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val viewModel: SettingsViewModel by viewModels()

    @Inject
    lateinit var settingsManager: SettingsManager

    override fun setUpUI() {
        binding.toolbarSettings.titleTextViewToolbar.text = getString(R.string.settings)
    }

    override fun observeClicks() {
        binding.toolbarSettings.buttonBackImageViewToolbar.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.shareAppTextViewButtonSettings.setOnClickListener {
            settingsManager.shareApp()
        }
        binding.rateAppTextViewButtonSettings.setOnClickListener {
            settingsManager.openAppInMarket()
        }
        binding.feedbackTextViewButtonSettings.setOnClickListener {
            settingsManager.sandFeedback()
        }
        binding.privacyPolicyTextViewButtonSettings.setOnClickListener {
            settingsManager.openPrivacyPolicy()
        }
        binding.signOutButtonSettings.setOnClickListener {
            signOut()
        }
        binding.deleteButtonSettings.setOnClickListener {
            deleteDialog()
        }
    }

    private fun signOut() {
        viewModel.auth.signOut()
        val intent = Intent(requireContext(), SplashActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun deleteDialog() {
        FinanculatorDialog(
            context = requireContext(),
            title = getString(R.string.delete_account_title),
            message = getString(R.string.delete_account_message),
            icon = R.drawable.ic_baseline_error_24,
            content = listOf(
                FinanculatorDialog.FinanculatorDialogItem(
                    title = requireContext().resourcesCompat.getString(R.string.delete),
                    action = {
                        deleteAccount()
                    }
                ),
                FinanculatorDialog.FinanculatorDialogItem(
                    title = requireContext().resourcesCompat.getString(android.R.string.cancel),
                    action = {}
                )
            )
        ).createAndShow()
    }

    private fun deleteAccount() {
        displayMainActivityProgressBar(true)
        viewModel.auth.currentUser?.delete()?.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                val intent = Intent(requireContext(), SplashActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                requireContext().showToast(R.string.network_error_default)
            }
            displayMainActivityProgressBar(false)
        }
    }
}