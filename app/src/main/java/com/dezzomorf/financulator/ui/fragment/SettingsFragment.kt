package com.dezzomorf.financulator.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.FragmentSettingsBinding
import com.dezzomorf.financulator.extensions.showToast
import com.dezzomorf.financulator.ui.activity.SplashActivity
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val viewModel: SettingsViewModel by viewModels()

    override fun setUpUi() {
        binding.toolbarSettings.titleTextViewToolbar.text = getString(R.string.settings)
    }

    override fun observeClicks() {
        binding.toolbarSettings.buttonBackImageViewToolbar.setOnClickListener {
            findNavController().popBackStack()
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
        AlertDialog.Builder(requireContext())
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setTitle(getString(R.string.delete_account_title))
            .setMessage(getString(R.string.delete_account_message))
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                deleteAccount()
                dialog.cancel()
            }
            .show()
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