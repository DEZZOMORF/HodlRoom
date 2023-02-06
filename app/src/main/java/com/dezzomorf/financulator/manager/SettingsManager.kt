package com.dezzomorf.financulator.manager

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.util.ConstVal.MARKET_LINK
import com.dezzomorf.financulator.util.ConstVal.PRIVACY_POLICY_LINK
import com.dezzomorf.financulator.util.ConstVal.TERMS_OF_USE_LINK
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val appLink = MARKET_LINK + context.packageName

    fun openAppInMarket(): Boolean {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(appLink)
            flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(this)
        }
        return false
    }

    fun shareApp(): Boolean {
        val shareMessage = ("\n${context.getString(R.string.let_me_recommend_you_this_application)}\n\n $appLink").trimIndent()
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            Intent.createChooser(this, context.getString(R.string.choose_one)).apply {
                flags = FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this)
            }
            return false
        }
    }

    fun sandFeedback(): Boolean {
        Intent(Intent.ACTION_SENDTO).apply {
            val email = "mailto:${context.getString(R.string.support_email)}"
            val subject = "?subject=" + Uri.encode(context.getString(R.string.feedback))
            data = Uri.parse(email + subject)
            flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(this)
        }
        return false
    }

    fun openPrivacyPolicy(): Boolean {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(PRIVACY_POLICY_LINK)
            flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(this)
        }
        return false
    }

    fun openTermsAndConditions(): Boolean {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(TERMS_OF_USE_LINK)
            flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(this)
        }
        return false
    }
}