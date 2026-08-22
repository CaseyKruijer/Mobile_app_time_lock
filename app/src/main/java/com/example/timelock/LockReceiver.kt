package com.example.timelock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class LockReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (context == null) return

        val lockIntent = Intent(context, LockActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        context.startActivity(lockIntent)
    }
}