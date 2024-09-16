package com.example.geminiapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.geminiapp.MainActivity
import com.example.geminiapp.dataclass.Message
import com.example.geminiapp.databinding.ItemModelMessageBinding
import com.example.geminiapp.databinding.ItemUserMessageBinding
import java.util.Locale

class MessageAdapter (private val messages : MutableList<Message>, context: Context) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var tts: TextToSpeech? = null
    private var isSpeaking = false
    private var isMuted = false

    init {
        tts = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                tts?.language = Locale.getDefault()
            }
        }
    }

    class MessageViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = when (viewType) {
            0 -> ItemUserMessageBinding.inflate(inflater, parent, false)
            else -> ItemModelMessageBinding.inflate(inflater, parent, false)
        }
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int = messages.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        when (val binding = holder.binding) {
            is ItemUserMessageBinding -> {
                binding.userMessageTextView.text = message.text
            }

            is ItemModelMessageBinding -> {
                if (message.text?.isEmpty() == true) { // Check for placeholder message
                    binding.modelMessageTextView.visibility = View.GONE
                    binding.loadingAnimation.visibility = View.VISIBLE
                } else {
                    binding.modelMessageTextView.visibility = View.VISIBLE
                    binding.loadingAnimation.visibility = View.GONE
                    binding.modelMessageTextView.text = message.text
                    if (!message.isUser && !isMuted) { // Check if TTS is muted
                        speakOut(message.text)
                    }

                }
            }
        }

    }


     private fun speakOut(text: String?) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        isSpeaking = true
    }

    private fun stopSpeaking() {
        if (isSpeaking) {
            tts?.stop()
            isSpeaking = false
        }
    }

    fun muteTTS() {
        stopSpeaking()
        isMuted = true
    }

    fun unmuteTTS() {
        isMuted = false
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) 0 else 1
    }

    fun releaseTTS() {
        tts?.stop()
        tts?.shutdown()
    }

    fun addPlaceholderMessage() {
        messages.add(Message("", false)) // Add an empty message
        notifyItemInserted(messages.size - 1)
    }

    fun updateLastMessage(text: String) {
        val lastIndex = messages.size - 1
        if (lastIndex >= 0 && !messages[lastIndex].isUser) {
            messages[lastIndex] = Message(text, false)
            notifyItemChanged(lastIndex)
        }


    }
}








