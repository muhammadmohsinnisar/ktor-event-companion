package com.mohsin.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Speaker(
    val id: Int? = null,
    val name: String,
    val bio: String,
    val eventId: Int
)

object Speakers : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val bio = text("bio")
    val eventId = integer("event_id") references Events.id

    override val primaryKey = PrimaryKey(id)
}