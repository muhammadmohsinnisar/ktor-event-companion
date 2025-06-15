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
}
