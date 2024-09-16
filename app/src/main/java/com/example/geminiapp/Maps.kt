package com.example.geminiapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import org.json.JSONException
import org.json.JSONObject

object Maps {
    fun openMapsApp(context: Context, geoString: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=$geoString")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    }

    fun openMapsDirections(context: Context, placeName: String) {
        val gmmIntentUri =
            Uri.parse("google.navigation:q=$placeName")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    }

    fun openMapsGivenDirections(context: Context, origin: String, destination: String) {
        val uri =
            Uri.parse("https://www.google.com/maps/dir/?api=1&origin=$origin&destination=$destination&travelmode=driving")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        context.startActivity(intent)

    }

    fun callOpenMapsApp(context: Context, responseText: String) {
        try {
            val jsonObject = JSONObject(responseText)
            val location = jsonObject.getString("place")
            openMapsApp(context, location)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun callOpenMapsDirections(context: Context, responseText: String) {
        try {
            val jsonObject = JSONObject(responseText)
            val location = jsonObject.getString("place")
            openMapsDirections(context, location)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun callOpenMapsGivenDirections(context: Context, responseText: String) {
        try {
            val jsonObject = JSONObject(responseText)
            val origin = jsonObject.getString("origin")
            val destination = jsonObject.getString("destination")
            openMapsGivenDirections(context, origin, destination)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}