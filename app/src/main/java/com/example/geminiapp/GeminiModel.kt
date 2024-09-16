package com.example.geminiapp

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig

object GeminiModel {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = Constants.GEMINI_API_KEY,
        generationConfig = generationConfig {
            temperature = 1f
            topK = 64
            topP = 0.95f
            maxOutputTokens = 8192
            responseMimeType = "text/plain"
        }, systemInstruction =
        content {
            text(Constants.SYSTEM_INSTRUCTION)
        },
    )
    val chat = generativeModel.startChat(
        history = listOf(
            content(role = "user"){text("You are my personal assistant")},
            content(role = "model"){text("I am your personal assistant")}
        )
    )
}