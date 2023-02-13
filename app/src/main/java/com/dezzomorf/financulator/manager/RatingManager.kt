package com.dezzomorf.financulator.manager

import android.app.Activity
import android.util.Log
import com.dezzomorf.financulator.BuildConfig
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.extensions.resourcesCompat
import com.dezzomorf.financulator.ui.view.FinanculatorDialog
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RatingManager @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val settingsManager: SettingsManager,
    private val networkConnectionManager: NetworkConnectionManager
) {
    companion object {
        private val TAG = RatingManager::class.java.simpleName
        const val RATING_DIALOG_TIMER = 1000*60*60*24*2 //2 days
    }

    data class RatingFlowData(
        var isRatingFlowFinished: Boolean,
        var lastRatingRequest: Long
    )

    fun setupRatingFlags(activity: Activity) {
        val ratingFlowData = sharedPreferencesManager.getRatingFlowData()
        val timerState = ratingFlowData.lastRatingRequest + RATING_DIALOG_TIMER < Date().time
        if (!ratingFlowData.isRatingFlowFinished && networkConnectionManager.isConnected.value && timerState) {
            createMainDialog(activity)
        }
    }

    private fun createMainDialog(activity: Activity) {
        FinanculatorDialog(
            context = activity,
            title = activity.applicationContext.getString(R.string.rate_app),
            message = activity.applicationContext.getString(R.string.do_you_like_the_app),
            icon = R.drawable.baseline_favorite_24,
            content = listOf(
                FinanculatorDialog.FinanculatorDialogItem(
                    title = activity.applicationContext.resourcesCompat.getString(R.string.yes),
                    action = {
                        requestReviewFlow(activity)
                        sharedPreferencesManager.appRated()
                    }
                ),
                FinanculatorDialog.FinanculatorDialogItem(
                    title = activity.applicationContext.resourcesCompat.getString(R.string.later),
                    action = {
                        sharedPreferencesManager.resetLastRatingRequest()
                    }
                ),
                FinanculatorDialog.FinanculatorDialogItem(
                    title = activity.applicationContext.resourcesCompat.getString(R.string.never),
                    action = {
                        sharedPreferencesManager.appRated()
                    }
                )
            )
        ).createAndShow()
    }

    private fun requestReviewFlow(activity: Activity) {
        val manager = if (BuildConfig.DEBUG) {
            FakeReviewManager(activity.applicationContext) // Fake for testing purposes
        } else {
            ReviewManagerFactory.create(activity.applicationContext)
        }

        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                manager.launchReviewFlow(activity, reviewInfo)
            } else {
                Log.e(TAG, "Rating task isn't successful: ${task.exception?.message}")
                settingsManager.openAppInMarket()
            }
        }
    }
}