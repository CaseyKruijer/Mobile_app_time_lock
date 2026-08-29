package com.example.timelock

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import java.util.Calendar

class TimeLockService : Service() {

    private val handler = Handler(Looper.getMainLooper())

    // Voor testen: iedere minuut controleren
    private val checkInterval = 60_000L

    private var lockScreenShown = false

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

            if (!lockScreenShown) {
                openLockScreen()
                lockScreenShown = true
            }

        } else {

            // Vanaf 07:00 mag de LockActivity zichzelf sluiten.
            lockScreenShown = false
        }
    }

    private fun openLockScreen() {

        val intent = Intent(
            this,
            LockActivity::class.java
        )

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(
            this,
            100,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val notification = Notification.Builder(
            this,
            "timelock_lock"
        )
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .setContentTitle("TimeLock")
            .setContentText("TimeLock is actief")
            .setPriority(Notification.PRIORITY_MAX)
            .setCategory(Notification.CATEGORY_ALARM)
            .setFullScreenIntent(
                pendingIntent,
                true
            )
            .setAutoCancel(true)
            .build()

        val manager =
            getSystemService(
                NotificationManager::class.java
            )

        manager.notify(
            LOCK_NOTIFICATION_ID,
            notification
        )
    }

    private fun createNotificationChannel() {

        /*
         * Channel voor de foreground service.
         */
        val serviceChannel = NotificationChannel(
            "timelock_service",
            "TimeLock service",
            NotificationManager.IMPORTANCE_LOW
        )

        /*
         * Apart HIGH-importance channel voor
         * het fullscreen lockscherm.
         */
        val lockChannel = NotificationChannel(
            "timelock_lock",
            "TimeLock lockscherm",
            NotificationManager.IMPORTANCE_HIGH
        )

        val manager =
            getSystemService(
                NotificationManager::class.java
            )

        manager.createNotificationChannel(
            serviceChannel
        )

        manager.createNotificationChannel(
            lockChannel
        )
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

        handler.removeCallbacks(
            timeChecker
        )

        super.onDestroy()
    }

    override fun onBind(
        intent: Intent?
    ): IBinder? {
        return null
    }

    companion object {

        private const val LOCK_NOTIFICATION_ID = 1001
    }
}