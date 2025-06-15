package com.mohsin.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Session(
    val id: Int,
    val title: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val eventId: Int,    // FK to Event
    val speakerId: Int   // FK to Speaker
)


object Sessions : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 100)
    val description = text("description")
    val startTime = varchar("start_time", 50)
    val endTime = varchar("end_time", 50)
    val eventId = integer("event_id").references(Events.id)
    val speakerId = integer("speaker_id").references(Speakers.id)

    override val primaryKey = PrimaryKey(id)
}



