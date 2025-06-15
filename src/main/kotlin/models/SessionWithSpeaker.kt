package com.mohsin.models

@kotlinx.serialization.Serializable
data class SessionWithSpeaker(
    val id: Int,
    val title: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val eventId: Int,
    val speaker: Speaker
)

