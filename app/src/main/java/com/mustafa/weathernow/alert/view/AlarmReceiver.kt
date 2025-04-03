package com.mustafa.weathernow.alert.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.mustafa.weathernow.utils.AlarmBroadcastReceiverConstants.ALARM_ACTION

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ALARM_ACTION) {
            Toast.makeText(context, "Alarm Received", Toast.LENGTH_SHORT).show()
        }
    }
}