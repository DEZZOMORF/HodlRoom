package com.lampa.financulator.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.lampa.financulator.R
import com.lampa.financulator.databinding.ActivityMainBinding
import com.lampa.financulator.extensions.resourcesCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        window.statusBarColor = resourcesCompat.getColor(R.color.purple_700)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun displayProgressBar(isDisplayed:Boolean) {
        binding.progressBar.isVisible = isDisplayed
    }
}