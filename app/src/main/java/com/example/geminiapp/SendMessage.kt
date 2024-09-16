package com.example.geminiapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.geminiapp.CallContact.REQUEST_CALL_PERMISSION
import org.json.JSONException
import org.json.JSONObject

object SendMessage {

    val REQUEST_SEND_SMS_PERMISSION = 1
    val REQUEST_RECORD_AUDIO_PERMISSION = 200

    private fun sendMessage(context: Context, name: String, message: String) {
        val phoneNumber = getContactNumber(context, name)
        if (phoneNumber != null) {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("sms:$phoneNumber")
                putExtra("sms_body", message)
            }

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Send SMS permission is not granted.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Contact not found.", Toast.LENGTH_SHORT).show()
        }
    }

    fun callSendMessage(context: Context, responseText: String){
    try {
        val jsonObject = JSONObject(responseText)
        val name = jsonObject.getString("name")
        val message = jsonObject.getString("message")
        sendMessage(context, name, message)
    }catch (e: JSONException) {
        e.printStackTrace()
    }
    }

    private fun getContactNumber(context: Context, name: String): String? {
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            "${ContactsContract.Contacts.DISPLAY_NAME} = ?",
            arrayOf(name),
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val contactId = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val phonesCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                    arrayOf(contactId),
                    null
                )

                phonesCursor?.use { phones ->
                    if (phones.moveToFirst()) {
                        return phones.getString(phones.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                }
            }
        }
        return null
    }

    fun requestSendSMSPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.SEND_SMS),
                REQUEST_SEND_SMS_PERMISSION)

        }else{
            checkAudioPermissions(activity)
        }

    }
    fun checkAudioPermissions(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO_PERMISSION)
        }
    }
}
