package com.dezzomorf.financulator.ui.activity.intent

import android.content.Context
import android.content.Intent
import com.dezzomorf.financulator.ui.activity.AuthorizationActivity
import com.dezzomorf.financulator.ui.activity.MainActivity

class AuthorizationActivityIntent (
    context: Context,
    val data: Data
) : Intent(context, AuthorizationActivity::class.java) {

    private enum class Key {
        LOGIN
    }

    class Data(
        val login: Boolean
    ) {
        constructor(intent: Intent) : this(
            intent.getBooleanExtra(Key.LOGIN.name, false)
        )
    }

    init {
        putExtra(Key.LOGIN.name, data.login)
    }
}