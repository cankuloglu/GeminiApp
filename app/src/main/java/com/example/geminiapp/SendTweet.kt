package com.example.geminiapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import org.json.JSONException
import org.json.JSONObject

object SendTweet {
    fun openTwitterToTweet(context: Context, tweetText: String) {
        val tweetUrl = "https://twitter.com/intent/tweet?text=${Uri.encode(tweetText)}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl))
        context.startActivity(intent)
    }

    fun callOpenTwitterToTweet(context: Context, responseText: String) {
        try {
            val jsonObject = JSONObject(responseText)
            val tweetText = jsonObject.getString("tweetText")
            SendTweet.openTwitterToTweet(context, tweetText)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
