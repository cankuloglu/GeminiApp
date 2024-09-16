package com.example.geminiapp

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.util.Calendar

object AddEvent {

     fun addEventToCalendar(context: Context, day: Int, month: Int, year: Int, startHour: Int, startMinute: Int,endHour: Int, endMinute: Int, title: String,location: String) {
        val startTime = Calendar.getInstance().apply {
            set(year, month, day, startHour, endMinute)
        }.timeInMillis

        val endTime = Calendar.getInstance().apply {
            set(year, month, day, endHour, endMinute)
        }

        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, title)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
            putExtra(CalendarContract.Events.EVENT_LOCATION, location)

        }
            context.startActivity(intent)

    }

    fun callAddEventToCalendar(context: Context, responseText: String) {
        try {
            val jsonObject = JSONObject(responseText)
            val day = jsonObject.optInt("day", 0)
            val month = jsonObject.optInt("month", 0)
            val year = jsonObject.optInt("year", 0)
            val startHour = jsonObject.optInt("startHour", 0)
            val startMinute = jsonObject.optInt("startMinute", 0)
            val endHour = jsonObject.optInt("endHour", 0)
            val endMinute = jsonObject.optInt("endMinute", 0)
            val title = jsonObject.optString("title", "No Title")
            val location = jsonObject.optString("eventLocation", "No Location")
            addEventToCalendar(
                context,
                day,
                month,
                year,
                startHour,
                startMinute,
                endHour,
                endMinute,
                title,
                location
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


}