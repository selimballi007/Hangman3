package org.hangman3

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.google.android.gms.ads.MobileAds
import org.hangman3.ads.AndroidAdHolder

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // shared içindeki holder'ı set et
        AndroidAdHolder.appContext = applicationContext

        // initialize AdMob
        MobileAds.initialize(this)

        // aktif activity'yi takip et
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {
                AndroidAdHolder.currentActivity = activity
            }

            override fun onActivityPaused(activity: Activity) {
                if (AndroidAdHolder.currentActivity == activity) AndroidAdHolder.currentActivity = null
            }

            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {
                if (AndroidAdHolder.currentActivity == activity) AndroidAdHolder.currentActivity = null
            }
        })
    }
}