package com.example.forecast.data.database.entity

/*
 * Class for request object
 */
data class Request(
    val type: String,
    val query: String,
    val language: String,
    val unit: String
)