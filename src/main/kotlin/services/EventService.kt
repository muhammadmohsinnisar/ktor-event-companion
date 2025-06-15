package com.mohsin.services

import com.mohsin.models.Event
import com.mohsin.models.EventDetail
import com.mohsin.models.Events
import com.mohsin.models.SessionWithSpeaker
import com.mohsin.models.Sessions
import com.mohsin.models.Speaker
import com.mohsin.models.Speakers
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class EventService {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(event: Event): Int = dbQuery {
        Events.insert {
            it[name] = event.name
            it[description] = event.description
            it[date] = event.date
            it[location] = event.location
        } get Events.id
    }

    suspend fun getAll(): List<Event> = dbQuery {
        Events.selectAll().map { row ->
            Event(
                id = row[Events.id],
                name = row[Events.name],
                description = row[Events.description],
                date = row[Events.date],
                location = row[Events.location]
            )
        }
    }

    suspend fun getById(id: Int): Event? = dbQuery {
        Events
            .selectAll()
            .where { Events.id eq id }
            .map { row ->
                Event(
                    id = row[Events.id],
                    name = row[Events.name],
                    description = row[Events.description],
                    date = row[Events.date],
                    location = row[Events.location]
                )
            }
            .singleOrNull()
    }

    suspend fun delete(id: Int): Boolean = dbQuery {
        Events.deleteWhere { Events.id eq id } > 0
    }

    suspend fun getEventWithDetails(id: Int): EventDetail? = dbQuery {
        val eventRow = Events
            .selectAll()
            .where { Events.id eq id }.singleOrNull() ?: return@dbQuery null

        val sessionsWithSpeakers = (Sessions innerJoin Speakers)
            .selectAll()
            .where{ Sessions.eventId eq id }
            .map {
                val speaker = Speaker(
                    id = it[Speakers.id],
                    name = it[Speakers.name],
                    bio = it[Speakers.bio],
                    eventId = it[Speakers.eventId]
                )
                SessionWithSpeaker(
                    id = it[Sessions.id],
                    title = it[Sessions.title],
                    description = it[Sessions.description],
                    startTime = it[Sessions.startTime],
                    endTime = it[Sessions.endTime],
                    eventId = it[Sessions.eventId],
                    speaker = speaker
                )
            }

        EventDetail(
            id = eventRow[Events.id],
            name = eventRow[Events.name],
            description = eventRow[Events.description],
            date = eventRow[Events.date],
            location = eventRow[Events.location],
            sessions = sessionsWithSpeakers
        )
    }

}
