package com.mohsin.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.Table

@Serializable
data class Event(
    val id: Int? = null,
    val name: String,
    val description: String,
    val date: String,
    val location: String
)

object Events : Table() {

    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val description = text("description")
    val date = varchar("date", 30)
    val location = varchar("location", 100)

    override val primaryKey = PrimaryKey(id)
}
