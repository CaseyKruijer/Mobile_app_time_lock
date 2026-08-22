package com.example.timelock

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import java.util.Calendar

class TimeLockService : Service() {

    private val handler = Handler(Looper.getMainLooper())

    private val checkInterval = 60_000L // 1 minuut

    private val timeChecker = object : Runnable {

        override fun run() {

            checkTime()

            handler.postDelayed(
                this,
                checkInterval
            )
        }
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        val notification = createNotification()

        startForeground(
            1,
            notification
        )

        // Meteen controleren
        handler.post(timeChecker)
    }

    private fun checkTime() {

        val calendar = Calendar.getInstance()

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val currentMinutes =
            hour * 60 + minute

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

        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        )

        startActivity(intent)
    }

    private fun createNotificationChannel() {

        val channel = NotificationChannel(
            "timelock_service",
            "TimeLock",
            NotificationManager.IMPORTANCE_LOW
        )

        val manager =
            getSystemService(
                NotificationManager::class.java
            )

        manager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {

        return Notification.Builder(
            this,
            "timelock_service"
        )
            .setContentTitle("TimeLock actief")
            .setContentText(
                "TimeLock controleert de tijd."
            )
            .setSmallIcon(
                android.R.drawable.ic_lock_lock
            )
            .build()
    }

    override fun onDestroy() {

        handler.removeCallbacks(timeChecker)

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}