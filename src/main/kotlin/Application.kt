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
    // Expect format: postgres://user:password@host:port/dbname
    val dbUrl = System.getenv("DATABASE_URL") ?: error("DATABASE_URL not found")

    val uri = java.net.URI(dbUrl)
    val userInfo = uri.userInfo?.split(":") ?: error("Missing user info in DATABASE_URL")
    val username = userInfo[0]
    val password = userInfo[1]
    val host = uri.host
    val port = uri.port
    val database = uri.path.removePrefix("/")

    val jdbcUrl = "jdbc:postgresql://$host:$port/$database"
    println("Connecting to: $jdbcUrl")
    Database.connect(
        url = jdbcUrl,
        driver = "org.postgresql.Driver",
        user = username,
        password = password
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

