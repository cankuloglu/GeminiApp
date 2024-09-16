package com.example.geminiapp

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.geminiapp.googlesearchapi.GoogleSearchApi
import com.example.geminiapp.googlesearchapi.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GoogleSearch {
    private val retrofitGoogle: Retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val googleSearchApi: GoogleSearchApi = retrofitGoogle.create(GoogleSearchApi::class.java)
    suspend fun callGoogleSearch(context: Context, responseText: String) {
        try {
            val jsonObject = JSONObject(responseText)
            val query = jsonObject.getString("query")
            val searchResults = withContext(Dispatchers.IO) {
            googleSearchApi.search(Constants.GOOGLE_API_KEY, Constants.SEARCH_ENGINE_ID, query)
        }
            withContext(Dispatchers.Main) {
                handleSearchResults(context,searchResults)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    private fun handleSearchResults(context: Context, searchResults: SearchResult) {
        if (!searchResults.items.isNullOrEmpty()) {
            val firstLink = searchResults.items[0].link
            if (firstLink != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(firstLink))
                context.startActivity(intent)
            }
        }
    }

}