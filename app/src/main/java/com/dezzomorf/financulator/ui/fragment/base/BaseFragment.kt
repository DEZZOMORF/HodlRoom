package com.dezzomorf.financulator.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.dezzomorf.financulator.ui.DisplayProgressBar

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment(), DisplayProgressBar {

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate.invoke(inflater, container, false)
        configureDataBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        observeClicks()
        setUpAdapters()
        observeAdapters()
        observeViewModel()
        setUpSwipeToRefresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected open fun configureDataBinding() {}
    protected open fun setUpUI() {}
    protected open fun setUpSwipeToRefresh() {}
    protected open fun setUpAdapters() {}
    protected open fun observeAdapters() {}
    protected open fun observeViewModel() {}
    protected open fun observeClicks() {}
}