package com.mohsin.services

import com.mohsin.models.Session
import com.mohsin.models.SessionWithSpeaker
import com.mohsin.models.Sessions
import com.mohsin.models.Speaker
import com.mohsin.models.Speakers
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class SessionService {
    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(session: Session): Int = dbQuery {
        Sessions.insert {
            it[title] = session.title
            it[description] = session.description
            it[startTime] = session.startTime
            it[endTime] = session.endTime
            it[eventId] = session.eventId
            it[speakerId] = session.speakerId
        }[Sessions.id]
    }

    suspend fun getByEvent(eventId: Int): List<Session> = dbQuery {
        Sessions
            .selectAll()
            .where { Sessions.eventId eq eventId }
            .map {
                Session(
                    id = it[Sessions.id],
                    title = it[Sessions.title],
                    description = it[Sessions.description],
                    startTime = it[Sessions.startTime],
                    endTime = it[Sessions.endTime],
                    eventId = it[Sessions.eventId],
                    speakerId = it[Sessions.speakerId]
                )
            }
    }

    suspend fun getAllSessions(): List<Session> = dbQuery {
        Sessions.selectAll().map {
            Session(
                id = it[Sessions.id],
                title = it[Sessions.title],
                description = it[Sessions.description],
                startTime = it[Sessions.startTime],
                endTime = it[Sessions.endTime],
                eventId = it[Sessions.eventId],
                speakerId = it[Sessions.speakerId]
            )
        }
    }

    suspend fun getByEventWithSpeakers(eventId: Int): List<SessionWithSpeaker> = dbQuery {
        (Sessions innerJoin Speakers)
            .selectAll()
            .where { Sessions.eventId eq eventId }
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
    }

}
