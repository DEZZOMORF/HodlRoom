package com.lampa.financulator.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.lampa.financulator.R
import com.lampa.financulator.adapter.MyFragmentStateAdapter
import com.lampa.financulator.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewPager()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initViewPager() {
        with(binding) {
            pager.adapter = MyFragmentStateAdapter(this@MainActivity);
            TabLayoutMediator(tabs, pager) { tab, position ->
                tab.text = "Tab $position"
                pager.setCurrentItem(tab.position, true)
            }.attach()
        }
    }
}