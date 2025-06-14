package com.mohsin.services

import com.mohsin.models.Event
import com.mohsin.models.Events
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
        }[Events.id]
    }

    suspend fun getAll(): List<Event> = dbQuery {
        Events.selectAll().map {
            Event(
                id = it[Events.id],
                name = it[Events.name],
                description = it[Events.description],
                date = it[Events.date],
                location = it[Events.location]
            )
        }
    }

    suspend fun getById(id: Int): Event? = dbQuery {
        Events.select { Events.id eq id }
            .map {
                Event(
                    id = it[Events.id],
                    name = it[Events.name],
                    description = it[Events.description],
                    date = it[Events.date],
                    location = it[Events.location]
                )
            }
            .singleOrNull()
    }

    suspend fun delete(id: Int): Boolean = dbQuery {
        Events.deleteWhere { Events.id eq id } > 0
    }
}
