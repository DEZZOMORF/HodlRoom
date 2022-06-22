package com.lampa.financulator.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB: ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeClicks()
        setupAdapters()
        observeAdapters()
        observeViewModel()
        setupSwipeToRefresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected open fun setupSwipeToRefresh() {}
    protected open fun configureDataBinding() {}
    protected open fun setupAdapters() {}
    protected open fun observeAdapters() {}
    protected open fun observeViewModel() {}
    protected open fun observeClicks() {}
}