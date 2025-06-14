package com.mohsin

import com.mohsin.models.Events
import com.mohsin.routes.eventRoutes
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
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/ktor",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "your_password" // Replace with your password
    )

    // Create tables
    transaction {
        SchemaUtils.create(Events)
    }

    install(ContentNegotiation) {
        json()
    }

    routing {
        eventRoutes()
    }
}
