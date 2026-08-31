package com.example.timelock

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureKioskMode()

        setContent {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "TimeLock"
                )

                Text(
                    text = "22:00 → 07:00"
                )

                Button(
                    onClick = {
                        startTimeLock()
                    }
                ) {
                    Text(
                        text = "Activate TimeLock"
                    )
                }
            }
        }
    }

    private fun configureKioskMode() {

        val devicePolicyManager =
            getSystemService(DevicePolicyManager::class.java)

        val adminComponent = ComponentName(
            this,
            TimeLockDeviceAdminReceiver::class.java
        )

        if (
            devicePolicyManager.isDeviceOwnerApp(packageName)
        ) {

            devicePolicyManager.setLockTaskPackages(
                adminComponent,
                arrayOf(packageName)
            )

            devicePolicyManager.setLockTaskFeatures(
                adminComponent,
                DevicePolicyManager.LOCK_TASK_FEATURE_NONE
            )
        }
    }

    private fun startTimeLock() {

        val serviceIntent = Intent(
            this,
            TimeLockService::class.java
        )

        startForegroundService(serviceIntent)
    }
}