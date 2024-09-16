package com.example.geminiapp

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geminiapp.CallContact.REQUEST_CALL_PERMISSION
import com.example.geminiapp.CallContact.REQUEST_CONTACTS_PERMISSION
import com.example.geminiapp.CallContact.requestCallPermission
import com.example.geminiapp.CallContact.requestContactsPermission
import com.example.geminiapp.SendMessage.REQUEST_SEND_SMS_PERMISSION
import com.example.geminiapp.SendMessage.requestSendSMSPermission
import com.example.geminiapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.Locale
import com.example.geminiapp.adapter.MessageAdapter
import com.example.geminiapp.dataclass.Message

class MainActivity : AppCompatActivity() {
    private var textToSpeechActive = true
    private val REQUEST_CODE_OVERLAY_PERMISSION = 101
    private lateinit var binding: ActivityMainBinding
    private lateinit var messageAdapter: MessageAdapter
    private val messageList = mutableListOf<Message>()
    private var commandText: String = ""
    private val recognizedTextReceiver = object : BroadcastReceiver() {
       override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "processedCommand") {
                    val command = intent.getStringExtra("command") ?: ""
                if (command.startsWith("Hey Android", ignoreCase = true)) {
                    Log.d("MainActivity", "Received command: $command")
                    val newCommand =
                        command.replace("Hey Android", "", ignoreCase = true).trim()
                    messageList.add(Message(newCommand, true))
                    messageAdapter.notifyItemInserted(messageList.size - 1)

                    messageAdapter.addPlaceholderMessage()
                    binding.recyclerView.smoothScrollToPosition(messageList.size - 1)


                    sendMessageToModel(newCommand)
                }

            }

           when(intent?.action){
               "onReadyForSpeech" -> {
                   binding.messageImageView.visibility = View.GONE
                   binding.serviceReadyTextView.visibility = View.VISIBLE
                   binding.speechBeginningTextView.visibility = View.GONE
                   binding.speechEndTextView.visibility = View.GONE
                   binding.errorNoMatchTextView.visibility = View.GONE
               }
               "onBeginningOfSpeech" -> {
                   binding.messageImageView.visibility = View.GONE
                   binding.serviceReadyTextView.visibility = View.GONE
                   binding.speechBeginningTextView.visibility = View.VISIBLE
                   binding.speechEndTextView.visibility = View.GONE
                   binding.errorNoMatchTextView.visibility = View.GONE
               }
               "onEndOfSpeech" -> {
                   binding.messageImageView.visibility = View.GONE
                   binding.serviceReadyTextView.visibility = View.GONE
                   binding.speechBeginningTextView.visibility = View.GONE
                   binding.speechEndTextView.visibility = View.VISIBLE
                   binding.errorNoMatchTextView.visibility = View.GONE
               }
               "ERROR_NO_MATCH" -> {
                   binding.messageImageView.visibility = View.GONE
                   binding.serviceReadyTextView.visibility = View.GONE
                   binding.speechBeginningTextView.visibility = View.GONE
                   binding.speechEndTextView.visibility = View.GONE
                   binding.errorNoMatchTextView.visibility = View.VISIBLE
               }
               "serviceDestroyed" -> {
                   binding.messageImageView.visibility = View.GONE
                   binding.serviceReadyTextView.visibility = View.GONE
                   binding.speechBeginningTextView.visibility = View.GONE
                   binding.speechEndTextView.visibility = View.GONE
                   binding.errorNoMatchTextView.visibility = View.GONE
                   binding.voiceAssistantOffTextView.visibility = View.VISIBLE
                   binding.startServiceButton.visibility = View.VISIBLE
               }
           }
       }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.serviceReadyTextView.visibility = View.GONE
        binding.speechBeginningTextView.visibility = View.GONE
        binding.speechEndTextView.visibility = View.GONE
        binding.errorNoMatchTextView.visibility = View.GONE
        binding.startServiceButton.visibility = View.GONE
        binding.voiceAssistantOffTextView.visibility = View.GONE

        messageAdapter = MessageAdapter(messageList,this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = messageAdapter
        requestOverlayPermission()
        requestCallPermission(this)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Welcome!")
        builder.setMessage("If you want your voice to be sent to Gemini," +
                "you need to start your voice message with 'Hey Android'.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            startVoiceAssistantService(this)
        }
        val dialog = builder.create()
        dialog.setOnDismissListener {
            startVoiceAssistantService(this)
        }
        dialog.show()


        binding.voiceButton.setOnClickListener {
            if (textToSpeechActive) {
                messageAdapter.muteTTS()
                textToSpeechActive = false
                binding.voiceButton.setImageResource(R.drawable.baseline_volume_off_24)
            } else {
                messageAdapter.unmuteTTS()
                textToSpeechActive = true
                binding.voiceButton.setImageResource(R.drawable.baseline_volume_up_24)

            }
        }

        binding.startServiceButton.setOnClickListener {
            startVoiceAssistantService(this)
            binding.startServiceButton.visibility = View.GONE
            binding.voiceAssistantOffTextView.visibility = View.GONE
        }

        binding.promptButton.setOnClickListener {
            commandText = binding.commandText.text.toString()
            if (commandText.isBlank()) {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }else{
                    hideKeyboard(this)

                    messageList.add(Message(commandText, true))
                    messageAdapter.notifyItemInserted(messageList.size - 1)

                    messageAdapter.addPlaceholderMessage()
                    binding.recyclerView.smoothScrollToPosition(messageList.size - 1)

                    // Send the message to the model
                    sendMessageToModel(commandText)
                    binding.commandText.text.clear()
                }
        }

        val filter = IntentFilter("processedCommand")
        filter.addAction("onReadyForSpeech")
        filter.addAction("onBeginningOfSpeech")
        filter.addAction("onEndOfSpeech")
        filter.addAction("ERROR_NO_MATCH")
        filter.addAction("serviceDestroyed")
        ContextCompat.registerReceiver(this, recognizedTextReceiver, filter, ContextCompat.RECEIVER_EXPORTED)






    }


    private fun requestOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CALL_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    requestContactsPermission(this@MainActivity)
                } else {
                    // Permission denied
                }
            }
            REQUEST_CONTACTS_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    requestSendSMSPermission(this@MainActivity)
                } else {
                    // Permission denied
                }
            }
            REQUEST_SEND_SMS_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    SendMessage.checkAudioPermissions(this@MainActivity)
                } else {
                    // Permission denied
                }
            }
        }
    }




    private fun sendMessageToModel(message: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = GeminiModel.chat.sendMessage(message)
                response.text?.let {
                    if (it.contains("```json")) {
                        it.trim()
                            .removeSurrounding("```json", "```")
                            .trim()
                    }else{
                        null
                    }

                }
                withContext(Dispatchers.Main) {
                    if(response.text?.contains("intentType") == true) {
                        val jsonObject = JSONObject(response.text)
                        val geminiResponse = jsonObject.getString("response")
                        messageAdapter.updateLastMessage(geminiResponse ?: "No response")
                        response.text?.let { HandleCommand.handleCommand(this@MainActivity, it) }
                    }else{
                        messageAdapter.updateLastMessage(response.text ?: "No response")
                    }

                    binding.recyclerView.smoothScrollToPosition(messageList.size - 1)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    messageList.add(Message("Error: ${e.message}", false))
                    messageAdapter.notifyItemInserted(messageList.size - 1)
                    binding.recyclerView.smoothScrollToPosition(messageList.size - 1)
                }
            }
        }
    }


    private val result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val results =
                    result.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                            as ArrayList<String>?
                binding.commandText.setText(results!![0])
            }
        }

    private fun askSpeechInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(
                this,
                "Speech recognition is not available",
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something!")
            result.launch(intent)
        }


    }

    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity.currentFocus ?: View(activity)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        messageAdapter.releaseTTS()
        unregisterReceiver(recognizedTextReceiver)
        stopVoiceAssistantService(this)
    }

companion object {

    fun startVoiceAssistantService(context: Context) {
        val intent = Intent(context, VoiceAssistantService::class.java)
        intent.putExtra("input", "Start listening")
        context.startService(intent)
        Log.d("MainActivity", "Service started")
    }

    fun stopVoiceAssistantService(context: Context) {
        val intent = Intent(context, VoiceAssistantService::class.java)
        context.stopService(intent)
        Log.d("MainActivity", "Service stopped")
    }
}

}








