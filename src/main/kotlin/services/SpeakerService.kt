package com.mohsin.services

import com.mohsin.models.Speaker
import com.mohsin.models.Speakers
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class SpeakerService {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun getByEvent(eventId: Int): List<Speaker> = dbQuery {
        Speakers.selectAll()
            .where { Speakers.eventId eq eventId }
            .map {
                Speaker(
                    id = it[Speakers.id],
                    name = it[Speakers.name],
                    bio = it[Speakers.bio],
                    eventId = it[Speakers.eventId]
                )
            }
    }

    suspend fun getAll(): List<Speaker> = dbQuery {
        Speakers.selectAll().map { row ->
            Speaker(
                id = row[Speakers.id],
                name = row[Speakers.name],
                bio = row[Speakers.bio],
                eventId = row[Speakers.eventId]
            )
        }
    }

    suspend fun create(speaker: Speaker): Int = dbQuery {
        Speakers.insert {
            it[name] = speaker.name
            it[bio] = speaker.bio
            it[eventId] = speaker.eventId
        }[Speakers.id]
    }
}
