package com.lampa.financulator.ui.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lampa.financulator.R
import com.lampa.financulator.databinding.FragmentMainBinding
import com.lampa.financulator.ui.fragment.base.BaseFragment
import com.lampa.financulator.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()

    override fun observeClicks() {
        binding.toolbar.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_coinListFragment)
        }
    }
}