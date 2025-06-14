package com.mohsin.models

import kotlinx.serialization.Serializable

@Serializable
data class Speaker(
    val id: Int,
    val name: String,
    val bio: String,
    val eventId: Int // FK to Event
)
