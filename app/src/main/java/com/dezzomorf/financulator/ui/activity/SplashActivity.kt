package com.dezzomorf.financulator.ui.activity

import android.os.Bundle
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.ActivitySplashBinding
import com.dezzomorf.financulator.extensions.resourcesCompat
import com.dezzomorf.financulator.ui.activity.base.BaseActivity
import com.dezzomorf.financulator.ui.activity.intent.AuthorizationActivityIntent
import com.dezzomorf.financulator.ui.activity.intent.MainActivityIntent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = resourcesCompat.getColor(R.color.purple_700)
        checkIsUserAuthorized()
    }

    private fun checkIsUserAuthorized() {
        val mainActivityIntent = MainActivityIntent(this, MainActivityIntent.Data(true))
        val authorizationActivityIntent = AuthorizationActivityIntent(this, AuthorizationActivityIntent.Data(false))
        startActivity(
            when (false) { //TODO Set login state
                true -> mainActivityIntent
                false -> authorizationActivityIntent
            }
        )
        finish()
    }
}