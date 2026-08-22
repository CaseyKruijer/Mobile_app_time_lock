package com.example.timelock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class UnlockReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent?
    ) {

        Toast.makeText(
            context,
            "ALARM HEEFT GEWERKT!",
            Toast.LENGTH_LONG
        ).show()
    }
}