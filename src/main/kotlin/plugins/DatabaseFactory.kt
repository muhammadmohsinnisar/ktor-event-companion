package com.mohsin.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {
    val config = environment.config
    val url = config.property("postgres.url").getString()
    val user = config.property("postgres.user").getString()
    val password = config.property("postgres.password").getString()

    Database.connect(url = url, driver = "org.postgresql.Driver", user = user, password = password)
}
