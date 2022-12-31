package com.dezzomorf.financulator.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.ActivityMainBinding
import com.dezzomorf.financulator.extensions.resourcesCompat
import com.dezzomorf.financulator.ui.activity.base.BaseActivity
import com.dezzomorf.financulator.viewmodel.DataBaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController
    private val viewModel: DataBaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = resourcesCompat.getColor(R.color.background_color)
        viewModel.setUpIsConnectCollecting()
        setUpNavController()
    }

    fun displayProgressBar(isDisplayed: Boolean) {
        binding.progressBar.isVisible = isDisplayed
    }

    private fun setUpNavController() {
        navController = findNavController(R.id.nav_host_fragment)
    }
}