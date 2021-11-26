package com.lampa.financulator.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<DataBinding : ViewDataBinding> : Fragment() {

    lateinit var binding: DataBinding
    protected abstract val layoutResId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = this@BaseFragment
        configureDataBinding()
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

    protected open fun setupSwipeToRefresh() {}
    protected open fun configureDataBinding() {}
    protected open fun setupAdapters() {}
    protected open fun observeAdapters() {}
    protected open fun observeViewModel() {}
    protected open fun observeClicks() {}
}