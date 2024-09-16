package com.example.geminiapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import org.json.JSONException
import org.json.JSONObject

object Alarm {
    @SuppressLint("QueryPermissionsNeeded")
    fun setAlarm(context: Context, hour: Int, minute: Int) {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minute)
            putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        }
        context.startActivity(intent)

    }

    fun callAlarm(context: Context,responseText: String) {
        try {
            val jsonObject = JSONObject(responseText)
            val hour = jsonObject.getInt("hour")
            val minute = jsonObject.getInt("minute")
            setAlarm(context, hour, minute)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}