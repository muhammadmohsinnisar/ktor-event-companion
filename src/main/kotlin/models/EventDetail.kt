package com.mohsin.models

@kotlinx.serialization.Serializable
data class EventDetail(
    val id: Int,
    val name: String,
    val description: String,
    val date: String,
    val location: String,
    val sessions: List<SessionWithSpeaker>
)
