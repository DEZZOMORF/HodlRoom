package com.dezzomorf.financulator.ui.fragment

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.FragmentSettingsBinding
import com.dezzomorf.financulator.ui.activity.SplashActivity
import com.dezzomorf.financulator.ui.fragment.base.BaseFragment
import com.dezzomorf.financulator.viewmodel.MainViewModel
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
            viewModel.auth.signOut()
            val intent = Intent(requireContext(), SplashActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}