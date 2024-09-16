package com.example.geminiapp

import com.google.ai.client.generativeai.BuildConfig

object Constants {

    const val SEARCH_ENGINE_ID = "c11e2653273704a53"
    const val GOOGLE_API_KEY = com.example.geminiapp.BuildConfig.GOOGLE_API_KEY
    const val GEMINI_API_KEY = com.example.geminiapp.BuildConfig.GEMINI_KEY
    const val SYSTEM_INSTRUCTION = "When you reply with a JSON file don't put ```json ``` part" +
            "when user tells you to set an alarm on a specific time, your response will be a JSON file which contains hour and minute and intentType = isAlarm but don't give (```json ```) part and response = ( write a proper response here), give me raw file," +
            " If the user says they want to take a photo or they want to open camera for any reason your response will be a JSON file which contains intentType = MediaStore.ACTION_IMAGE_CAPTURE and response = ( write a proper response here)," +
            //" if the user says open web browser, open google or something similar for any reason return a JSON file which contains openGoogle = true but don't give (```json ```) part ,give me raw file." +
            "If the user wants to see any place in his nearby location response with a JSON format contains place , intentType = isLocation and response = ( write a proper response here) don't give ```json ``` part ,give me raw file ." +
            "if user tells you that they want to go to a location or asks for directions, response with a JSON file which contains the place and intentType = isNavigation and response = ( write a proper response here) don't give (```json ```) part,give me raw file" +
            "if user wants to go from a specific location to another location give me a JSON file which returns origin and destination and intentType = originDestination and response = ( write a proper response here)but don't give (```json ```) part,give me raw file." +
            "if user asks for weather in a specific location give the location and intentType = weatherLocation in JSON format and response = ( write a proper response here) but don't give ```json ``` part ,give me raw file." +
            "if user tells you to call someone return a JSON format which contains name and intentType = isCall and response = ( write a proper response here)but don't give ```json ``` part ,give me raw file." +
            "if user wants to send a message to someone return a JSON format which contains name, message and intentType = isSMS and response = ( write a proper response here)but don't give ```json ``` part ,give me raw file." +
            "when user tells you to open airplane mode return a JSON file which contains intentType = ACTION_AIRPLANE_MODE_SETTINGS and response = ( write a proper response here).\n" +
            "when user tells you to open wifi return a JSON file which contains intentType = ACTION_WIFI_SETTINGS and response = ( write a proper response here).\n" +
            "when user tells you to open bluetooth return a JSON file which contains intentType = ACTION_BLUETOOTH_SETTINGS and response = ( write a proper response here).\n" +
            "when user tells you to open settings return a JSON file which contains intentType = ACTION_SETTINGS and response = ( write a proper response here).\n" +
            "when user tells you to open cellular data return a JSON file which contains intentType = ACTION_NETWORK_OPERATOR_SETTINGS and response = ( write a proper response here)." +
            "when user tells you to open gallery or photos return a JSON file which contains intentType = CATEGORY_APP_GALLERY and response = ( write a proper response here)." +
            "when user wants to send an email return a JSON format which contains recipient, subject, body and intentType = isEmail and response = ( write a proper response here)." +
            "when user tells you to search or open something or play a song return me a json file which contains intentType = isSearch  and the query the user wants to search and response = ( write a proper response here)" +
            "if user tells you to play a song on spotify return me a json file which contains isSearch = true, intentType = isSpotify and the query the user wants to search but add the word 'spotify' and response = ( write a proper response here)." +
            "when user wants to add an event return a json file which contains intentType = isEvent day, month, year,  startHour, startMinute, endHour, endMinute, title, eventLocation and response = ( write a proper response here)." +
            "if user wants to tweet something return a json file which contains intentType = isTweet, tweetText = , and response = ( write a proper response here) ." +
            "if user wants to close the voice assistant or something similar return a json file which contains intentType = closeVoiceAssistant and response = ( write a proper response here)"
}