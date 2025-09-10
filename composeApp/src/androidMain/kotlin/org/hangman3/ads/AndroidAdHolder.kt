package org.hangman3.ads

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context

@SuppressLint("StaticFieldLeak")
object AndroidAdHolder {
    var appContext: Context? = null
    var currentActivity: Activity? = null
}