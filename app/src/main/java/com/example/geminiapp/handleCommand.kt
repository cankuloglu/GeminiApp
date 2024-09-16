package com.example.geminiapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import org.json.JSONObject

object HandleCommand {

    suspend fun handleCommand(context: Context, command: String) {
        if (command.contains("intentType")) {
            val jsonObject = JSONObject(command)
            val intentType = jsonObject.getString("intentType")
            when (intentType) {
                "isAlarm" -> Alarm.callAlarm(context, command)
                "MediaStore.ACTION_IMAGE_CAPTURE" -> openCamera(context,MediaStore.ACTION_IMAGE_CAPTURE)
                "isLocation" -> Maps.callOpenMapsApp(context, command)
                "isNavigation" -> Maps.callOpenMapsDirections(context, command)
                "originDestination" -> Maps.callOpenMapsGivenDirections(context, command)
                "weatherLocation" -> callShowWeather(context,command)
                "isCall" -> CallContact.callCallContact(context, command)
                "isSMS" -> SendMessage.callSendMessage(context, command)
                "ACTION_AIRPLANE_MODE_SETTINGS" -> SettingsIntents.openAirplaneModeSettings(context)
                "ACTION_WIFI_SETTINGS" -> SettingsIntents.openWifiSettings(context)
                "ACTION_BLUETOOTH_SETTINGS" -> SettingsIntents.openBluetoothSettings(context)
                "ACTION_SETTINGS" -> SettingsIntents.openSettings(context)
                "ACTION_NETWORK_OPERATOR_SETTINGS" -> SettingsIntents.openCellularDataSettings(context)
                "CATEGORY_APP_GALLERY" -> openGallery(context)
                "isEmail" -> SendEmail.callSendEmail(context, command)
                "isSearch" -> GoogleSearch.callGoogleSearch(context, command)
                "isSpotify" -> GoogleSearch.callGoogleSearch(context, command)
                "isEvent" -> AddEvent.callAddEventToCalendar(context, command)
                "isTweet" -> SendTweet.callOpenTwitterToTweet(context, command)
                "isCalendar" -> AddEvent.callAddEventToCalendar(context, command)
                "closeVoiceAssistant" -> MainActivity.stopVoiceAssistantService(context)
            }
        }


    }

    private fun openCamera(context: Context, action: String) {
        val intent = Intent(action).apply {
        }
        context.startActivity(intent)
    }

    private fun openGallery(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_APP_GALLERY)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    private fun callShowWeather(context: Context,responseText: String) {
        val jsonObject = JSONObject(responseText)
        val location = jsonObject.getString("location")
        showWeather(context,location)
    }
    private fun showWeather(context: Context,location: String){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("https://www.google.com/search?q=$location+hava+durumu"))
        context.startActivity(intent)
    }
}