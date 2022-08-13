package com.dezzomorf.financulator.ui.activity

import android.content.Intent
import android.os.Bundle
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.databinding.ActivitySplashBinding
import com.dezzomorf.financulator.extensions.resourcesCompat
import com.dezzomorf.financulator.ui.activity.base.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = resourcesCompat.getColor(R.color.purple_700)
        auth = Firebase.auth

        checkIsUserAuthorized()
    }

    private fun checkIsUserAuthorized() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        val authorizationActivityIntent = Intent(this, AuthorizationActivity::class.java)
        startActivity(
            when (auth.currentUser != null) {
                true -> mainActivityIntent
                false -> authorizationActivityIntent
            }
        )
        finish()
    }
}