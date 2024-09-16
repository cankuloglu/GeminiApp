package com.example.geminiapp.googlesearchapi

data class SearchResult(
    val items: List<Item>?
)

data class Item(
    val title: String?,
    val link: String?,
)
