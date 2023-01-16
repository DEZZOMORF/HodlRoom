package com.dezzomorf.financulator.ui.activity

import android.content.Intent
import android.os.Bundle
import com.dezzomorf.financulator.BuildConfig
import com.dezzomorf.financulator.databinding.ActivitySplashBinding
import com.dezzomorf.financulator.manager.NetworkConnectionManager
import com.dezzomorf.financulator.ui.activity.base.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity: BaseActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    @Inject
    lateinit var networkConnectionManager: NetworkConnectionManager
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth

        setUpCrashlytics()
        checkIsUserAuthorized()
    }

    private fun checkIsUserAuthorized() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        val authorizationActivityIntent = Intent(this, AuthorizationActivity::class.java)
        startActivity(
            when (auth.currentUser != null && auth.currentUser?.isEmailVerified == true) {
                true -> mainActivityIntent
                false -> authorizationActivityIntent
            }
        )
        finish()
    }

    private fun setUpCrashlytics() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }
}