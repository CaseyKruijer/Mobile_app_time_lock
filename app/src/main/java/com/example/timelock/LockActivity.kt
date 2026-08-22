package com.example.timelock

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import java.util.Calendar

class LockActivity : ComponentActivity() {

    private val handler = Handler(Looper.getMainLooper())

    // Controleer iedere minuut
    private val checkInterval = 60_000L

    private val timeChecker = object : Runnable {

        override fun run() {

            checkTime()

            handler.postDelayed(
                this,
                checkInterval
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideSystemUI()

        setContent {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),

                horizontalAlignment = Alignment.CenterHorizontally,

                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "🔒",
                    color = Color.White
                )

                Text(
                    text = "TIME LOCK",
                    color = Color.White
                )
            }
        }

        handler.post(timeChecker)
    }

    private fun checkTime() {

        val calendar = Calendar.getInstance()

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val currentMinutes = hour * 60 + minute

        // 22:00
        val lockTime = 22 * 60

        // 07:00
        val unlockTime = 7 * 60

        val shouldBeLocked =
            currentMinutes >= lockTime ||
                    currentMinutes < unlockTime

        if (!shouldBeLocked) {
            finish()
        }
    }

    private fun hideSystemUI() {

        val controller = window.insetsController

        if (controller != null) {

            controller.hide(
                WindowInsets.Type.statusBars() or
                        WindowInsets.Type.navigationBars()
            )

            controller.systemBarsBehavior =
                WindowInsetsController
                    .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onDestroy() {

        handler.removeCallbacks(timeChecker)

        super.onDestroy()
    }
}