package org.hangman3

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.ads.MobileAds
import org.hangman3.ads.AndroidAdHolder
import org.hangman3.data.AndroidUserDataRepository

class MainActivity : ComponentActivity() {

    private lateinit var repository: AndroidUserDataRepository

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        fullScreenFunctions()

        setContent {
            repository = AndroidUserDataRepository(this)
            App(repository)
        }
    }
}

private fun MainActivity.fullScreenFunctions() {
    WindowCompat.setDecorFitsSystemWindows(window, false)

    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}