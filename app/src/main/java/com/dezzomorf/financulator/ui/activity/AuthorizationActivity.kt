package com.dezzomorf.financulator.ui.activity

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.ActivityAuthorizationBinding
import com.dezzomorf.financulator.extensions.resourcesCompat
import com.dezzomorf.financulator.ui.activity.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorizationActivity : BaseActivity() {

    private val binding by lazy { ActivityAuthorizationBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = resourcesCompat.getColor(R.color.background_color)
        setUpNavController()
    }

    override fun onBackPressed() {
        finish()
    }

    fun displayProgressBar(isDisplayed: Boolean) {
        binding.progressBar.isVisible = isDisplayed
    }

    private fun setUpNavController() {
        navController = findNavController(R.id.nav_host_fragment)
    }
}