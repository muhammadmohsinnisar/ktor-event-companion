package com.mohsin.models

@kotlinx.serialization.Serializable
data class SessionCreateRequest(
    val title: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val speakerId: Int
)
