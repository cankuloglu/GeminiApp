package com.example.geminiapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import org.json.JSONException
import org.json.JSONObject

object SendEmail {
    fun sendEmail(context: Context,recipient: String,subject: String, body: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$recipient")
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        context.startActivity(intent)
    }

    fun callSendEmail(context: Context, responseText: String) {
        try {
            val jsonObject = JSONObject(responseText)
            val recipient = jsonObject.getString("recipient")
            val subject = jsonObject.getString("subject")
            val body = jsonObject.getString("body")
            sendEmail(context,recipient, subject, body)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
}