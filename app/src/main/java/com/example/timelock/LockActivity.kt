package com.example.timelock

import android.app.KeyguardManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
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

        /*
         * Scherm aanzetten en boven
         * lockscreen laten verschijnen.
         */
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        val keyguardManager =
            getSystemService(
                KeyguardManager::class.java
            )

        if (keyguardManager.isKeyguardLocked) {

            if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O
            ) {

                keyguardManager.requestDismissKeyguard(
                    this,
                    null
                )
            }
        }

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

        /*
         * Zodra LockActivity zichtbaar is,
         * wordt de telefoon kiosk-mode.
         */
        startKioskMode()

        handler.post(timeChecker)
    }

    private fun startKioskMode() {

        /*
         * Alleen uitvoeren wanneer TimeLock
         * daadwerkelijk Device Owner /
         * Lock Task toegestaan is.
         */
        if (!isLockTaskPermitted()) {
            return
        }

        startLockTask()
    }

    private fun isLockTaskPermitted(): Boolean {

        val devicePolicyManager =
            getSystemService(
                android.app.admin.DevicePolicyManager::class.java
            )

        return devicePolicyManager
            .isLockTaskPermitted(packageName)
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

        val lockTime =
            22 * 60

        val unlockTime =
            7 * 60

        val shouldBeLocked =
            currentMinutes >= lockTime ||
                    currentMinutes < unlockTime

        if (!shouldBeLocked) {

            /*
             * 07:00 bereikt.
             *
             * Eerst kiosk verlaten,
             * daarna Activity sluiten.
             */
            stopLockTask()

            finish()
        }
    }

    private fun hideSystemUI() {

        val controller =
            window.insetsController

        if (controller != null) {

            controller.hide(
                WindowInsets.Type.statusBars() or
                        WindowInsets.Type.navigationBars()
            )

            controller.systemBarsBehavior =
                WindowInsetsController
                    .BEHAVIOR_DEFAULT
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

    override fun onBackPressed() {
        /*
         * Back blokkeren.
         */
    }

    override fun onDestroy() {

        handler.removeCallbacks(
            timeChecker
        )

        super.onDestroy()
    }
}