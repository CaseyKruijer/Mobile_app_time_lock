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

    // Iedere minuut controleren
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

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        /*
         * Zorg dat het scherm aangaat
         * en boven het lockscreen kan verschijnen.
         */
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        /*
         * Als Android een beveiligd lockscreen heeft,
         * mag deze Activity het tonen vervangen.
         */
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

        // Meteen controleren
        handler.post(timeChecker)
    }

    override fun onWindowFocusChanged(
        hasFocus: Boolean
    ) {
        super.onWindowFocusChanged(hasFocus)

        /*
         * Android kan de system bars opnieuw tonen
         * nadat de Activity focus krijgt.
         *
         * Daarom opnieuw fullscreen instellen.
         */
        if (hasFocus) {
            hideSystemUI()
        }
    }

    private fun checkTime() {

        val calendar = Calendar.getInstance()

        val hour =
            calendar.get(Calendar.HOUR_OF_DAY)

        val minute =
            calendar.get(Calendar.MINUTE)

        val currentMinutes =
            hour * 60 + minute

        // 22:00
        val lockTime = 22 * 60

        // 07:00
        val unlockTime = 7 * 60

        val shouldBeLocked =
            currentMinutes >= lockTime ||
                    currentMinutes < unlockTime

        if (!shouldBeLocked) {

            /*
             * Vanaf 07:00 gaat het lockscherm
             * automatisch weg.
             */
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

            /*
             * GEEN swipe om de system bars
             * tijdelijk terug te krijgen.
             */
            controller.systemBarsBehavior =
                WindowInsetsController
                    .BEHAVIOR_DEFAULT
        }
    }

//    override fun OnBackPressedDispatcher() {
        /*
         * Back-knop blokkeren tijdens TimeLock.
         *
         * We doen hier bewust niets.
         */
//    }

    override fun onDestroy() {

        handler.removeCallbacks(
            timeChecker
        )

        super.onDestroy()
    }
}