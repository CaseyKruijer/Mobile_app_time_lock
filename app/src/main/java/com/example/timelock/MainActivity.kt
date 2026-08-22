package com.example.timelock

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.util.Calendar

class MainActivity : ComponentActivity() {

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

        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("TimeLock")
                Text("Unlocked")
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

        if (shouldBeLocked) {
            openLockScreen()
        }
    }

    private fun openLockScreen() {

        val intent = Intent(
            this,
            LockActivity::class.java
        )

        startActivity(intent)

        finish()
    }

    override fun onDestroy() {

        handler.removeCallbacks(timeChecker)

        super.onDestroy()
    }
}