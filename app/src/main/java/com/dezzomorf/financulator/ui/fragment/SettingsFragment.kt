package com.dezzomorf.financulator.ui.fragment

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.FragmentSettingsBinding
import com.dezzomorf.financulator.extensions.resourcesCompat
import com.dezzomorf.financulator.extensions.setClipboard
import com.dezzomorf.financulator.extensions.showToast
import com.dezzomorf.financulator.manager.RemoteConfigManager
import com.dezzomorf.financulator.manager.SettingsManager
import com.dezzomorf.financulator.ui.activity.SplashActivity
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.ui.view.FinanculatorDialog
import com.dezzomorf.financulator.util.RequestState
import com.dezzomorf.financulator.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    companion object {
        private val TAG = SettingsFragment::class.java.simpleName
    }

    private val viewModel: SettingsViewModel by viewModels()

    @Inject
    lateinit var settingsManager: SettingsManager

    @Inject
    lateinit var remoteConfigManager: RemoteConfigManager

    override fun setUpUI() {
        binding.toolbarSettings.titleTextViewToolbar.text = getString(R.string.settings)
    }

    override fun observeClicks() {
        binding.toolbarSettings.buttonBackImageViewToolbar.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.helpUsTextViewButtonSettings.setOnClickListener {
            helpUsDialog()
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
        binding.termsAndConditionsTextViewButtonSettings.setOnClickListener {
            settingsManager.openTermsAndConditions()
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

    private fun helpUsDialog() {
        when (val donationWalletsState = remoteConfigManager.getDonationWallets()) {
            is RequestState.Success -> {

                val dialogItemList = mutableListOf<FinanculatorDialog.FinanculatorDialogItem>()
                donationWalletsState.data.keys.forEach { coinName ->
                    donationWalletsState.data[coinName]?.let { wallet ->
                        val itemList = FinanculatorDialog.FinanculatorDialogItem(
                            title = coinName,
                            action = {
                                requireContext().setClipboard(wallet.toString())
                                Toast.makeText(requireContext(), getString(R.string.walled_copied), Toast.LENGTH_LONG).show()
                            }
                        )
                        dialogItemList.add(itemList)
                    }
                }

                //Cancel button
                dialogItemList.add(
                    FinanculatorDialog.FinanculatorDialogItem(
                        title = requireContext().resourcesCompat.getString(android.R.string.cancel),
                        action = {}
                    )
                )

                FinanculatorDialog(
                    context = requireContext(),
                    title = getString(R.string.support_us),
                    icon = R.drawable.baseline_favorite_24,
                    message = getString(R.string.we_need_your_help),
                    content = dialogItemList
                ).createAndShow()
            }
            is RequestState.GeneralError -> {
                Log.e(TAG, donationWalletsState.exception.message ?: getString(R.string.network_error_default))
                Toast.makeText(requireContext(), getString(R.string.network_error_default), Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }
}