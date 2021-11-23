package com.lampa.financulator.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lampa.financulator.databinding.PortfolioGraphFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PortfolioGraphFragment : Fragment() {

    private var _binding: PortfolioGraphFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = PortfolioGraphFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}