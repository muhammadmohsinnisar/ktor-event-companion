package com.mohsin.models

import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val id: Int,
    val title: String,
    val startTime: String,
    val endTime: String,
    val eventId: Int // FK to Event
)
