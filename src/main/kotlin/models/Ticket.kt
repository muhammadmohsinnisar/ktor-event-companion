package com.mohsin.models

import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    val id: Int,
    val eventId: Int,
    val qrCode: String,
    val issuedTo: String
)
