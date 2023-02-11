package com.dezzomorf.financulator.manager

import android.app.Activity
import android.widget.Toast
import com.dezzomorf.financulator.R
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class InAppUpdateManager(private val activity: Activity) : InstallStateUpdatedListener {

    companion object {
        private const val UPDATE_REQUEST_CODE = 500
        private const val DAYS_FOR_FLEXIBLE_UPDATE = 30
    }

    private var parentActivity: Activity = activity
    private var appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(parentActivity)
    private val appUpdateInfoTask = appUpdateManager.appUpdateInfo
    private var currentType = AppUpdateType.FLEXIBLE
    private var isDownloadingMessageShowed = false

    init {
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if ((appUpdateInfo.clientVersionStalenessDays() ?: -1) >= DAYS_FOR_FLEXIBLE_UPDATE) {
                    startUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE)
                } else {
                    startUpdate(appUpdateInfo, AppUpdateType.FLEXIBLE)
                }
            }
        }
    }

    override fun onStateUpdate(state: InstallState) {
        when (state.installStatus()) {
            InstallStatus.DOWNLOADING -> {
                if (!isDownloadingMessageShowed) {
                    showDownloadingUpdateToast()
                    isDownloadingMessageShowed = true
                }
            }
            InstallStatus.DOWNLOADED -> appUpdateManager.completeUpdate()
            else -> {}
        }
    }

    private fun showDownloadingUpdateToast() {
        Toast.makeText(parentActivity, activity.getString(R.string.downloading_update), Toast.LENGTH_LONG).show()
    }

    private fun startUpdate(info: AppUpdateInfo, type: Int) {
        appUpdateManager.startUpdateFlowForResult(info, type, parentActivity, UPDATE_REQUEST_CODE)
        currentType = type
    }

    fun onResume() {
        appUpdateManager.registerListener(this)
    }

    fun onDestroy() {
        appUpdateManager.unregisterListener(this)
    }
}