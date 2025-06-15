package com.mohsin

import com.mohsin.models.Events
import com.mohsin.models.Sessions
import com.mohsin.models.Speakers
import com.mohsin.routes.eventRoutes
import com.mohsin.routes.sessionRoutes
import com.mohsin.routes.speakerRoutes
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    // Connect to PostgreSQL
    val dbUrl = System.getenv("DATABASE_URL") ?: error("DATABASE_URL not found")

    val jdbcUrl = "jdbc:postgresql://${dbUrl.removePrefix("postgres://")
        .replace(":", ":")
        .replace("@", "/")}?sslmode=require"

    val uri = java.net.URI(dbUrl)
    val userInfo = uri.userInfo.split(":")

    Database.connect(
        url = jdbcUrl,
        driver = "org.postgresql.Driver",
        user = userInfo[0],
        password = userInfo[1]
    )


    // Create tables
    transaction {
        SchemaUtils.createMissingTablesAndColumns(Events, Speakers, Sessions)
    }

    install(ContentNegotiation) {
        json()
    }

    routing {
        eventRoutes()
        speakerRoutes()
        sessionRoutes()
    }
}
