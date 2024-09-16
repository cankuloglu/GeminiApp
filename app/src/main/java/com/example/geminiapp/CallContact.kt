package com.example.geminiapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.json.JSONException
import org.json.JSONObject

object CallContact {

    val REQUEST_CONTACTS_PERMISSION = 1
    val REQUEST_CALL_PERMISSION = 2


    private fun callContact(context: Context, name: String) {
        val phoneNumber = getContactNumber(context,name)
        if (phoneNumber != null) {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Call permission is not granted.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Contact not found.", Toast.LENGTH_SHORT).show()
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

    fun callCallContact(context: Context, responseText: String){
        try {
            val jsonObject = JSONObject(responseText)
            val name = jsonObject.getString("name")
            callContact(context, name)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun requestContactsPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CONTACTS_PERMISSION
            )
        }else{
            SendMessage.requestSendSMSPermission(activity)
        }


    }

     fun requestCallPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CALL_PERMISSION
            )
        }else{
            requestContactsPermission(activity)
        }
    }
}