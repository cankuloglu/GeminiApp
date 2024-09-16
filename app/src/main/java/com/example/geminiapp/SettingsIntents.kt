package com.example.geminiapp

import android.content.Context
import android.content.Intent
import android.provider.Settings

object SettingsIntents {

    fun openBluetoothSettings(context: Context) {
        val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
        context.startActivity(intent)
    }
    fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_SETTINGS)
        context.startActivity(intent)
    }
    fun openAirplaneModeSettings(context: Context) {
        val intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
        context.startActivity(intent)
    }
    fun openWifiSettings(context: Context) {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        context.startActivity(intent)
    }
    fun openCellularDataSettings(context: Context) {
        val intent = Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
        context.startActivity(intent)
    }




}