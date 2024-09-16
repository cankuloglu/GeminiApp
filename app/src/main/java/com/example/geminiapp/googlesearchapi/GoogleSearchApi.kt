package com.example.geminiapp.googlesearchapi

import retrofit2.http.GET
import retrofit2.http.Query
interface GoogleSearchApi {
    @GET("customsearch/v1")
    suspend fun search(
        @Query("key") apiKey: String,
        @Query("cx") searchEngineId: String,
        @Query("q") query: String
    ): SearchResult
}