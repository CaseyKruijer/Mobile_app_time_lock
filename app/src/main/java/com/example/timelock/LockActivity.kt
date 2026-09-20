package com.example.timelock

import android.app.admin.DevicePolicyManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
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

    private val handler =
        Handler(Looper.getMainLooper())

    private val checkInterval =
        60_000L

    private val timeChecker =
        object : Runnable {

            override fun run() {

                checkTime()

                handler.postDelayed(
                    this,
                    checkInterval
                )
            }
        }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        hideSystemUI()

        setContent {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),

                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.Center
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

        startKioskMode()

        handler.post(timeChecker)
    }

    private fun startKioskMode() {

        val devicePolicyManager =
            getSystemService(
                DevicePolicyManager::class.java
            )

        if (
            devicePolicyManager.isLockTaskPermitted(
                packageName
            )
        ) {

            startLockTask()
        }
    }

    private fun checkTime() {

        val calendar =
            Calendar.getInstance()

        val hour =
            calendar.get(Calendar.HOUR_OF_DAY)

        val minute =
            calendar.get(Calendar.MINUTE)

        val currentMinutes =
            hour * 60 + minute

        // Lock starts at 22:00
        val lockTime =
            22 * 60

        // Unlock starts at 07:00
        val unlockTime =
            7 * 60

        val shouldBeLocked =
            currentMinutes >= lockTime ||
                    currentMinutes < unlockTime

        if (!shouldBeLocked) {

            stopLockTask()

            finish()
        }
    }

    private fun hideSystemUI() {
        window.decorView.post {
            val controller = window.decorView.windowInsetsController

            controller?.hide(
                WindowInsets.Type.statusBars() or
                        WindowInsets.Type.navigationBars()
            )
        }
    }

    override fun onWindowFocusChanged(
        hasFocus: Boolean
    ) {

        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) {
            hideSystemUI()
        }
    }

    override fun onDestroy() {

        handler.removeCallbacks(
            timeChecker
        )

        super.onDestroy()
    }
}