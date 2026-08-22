package com.example.timelock

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen(
                onOpenLock = {

                    val intent =
                        Intent(this, LockActivity::class.java)

                    startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun MainScreen(
    onOpenLock: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        Text("TimeLock")

        Button(
            onClick = onOpenLock
        ) {
            Text("Open Lock Screen")
        }
    }
}