@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.timelock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.util.Calendar

class MainActivity : ComponentActivity() {

//  Tijden dat op slot gaat
//    private val lockHour = 22
//    private val lockMinute = 0
//
//    private val unlockHour = 7
//    private val unlockMinute = 0

    private val lockHour = 16
    private val lockMinute = 32

    private val unlockHour = 16
    private val unlockMinute = 35

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TimeLockScreen(
                onActivate = {
                    scheduleLockAndUnlock()
                }
            )
        }
    }

    private fun scheduleLockAndUnlock() {
        Toast.makeText(
            this,
            "ACTIVATE werkt!",
            Toast.LENGTH_LONG
        ).show()
    }

    @Composable
    fun TimeLockScreen(
        onActivate: () -> Unit
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Time Lock"
            )

            Text(
                text = "22:00 → 07:00"
            )

            Button(
                onClick = onActivate
            ) {
                Text("Activate")
            }
        }
    }
}