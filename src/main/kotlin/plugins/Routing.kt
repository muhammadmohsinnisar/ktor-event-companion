package com.mohsin.plugins

import com.mohsin.services.EventService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import com.mohsin.models.Event

fun Application.configureRouting(eventService: EventService) {
    routing {
        route("/events") {
            get {
                call.respond(eventService.getAll())
            }

            post {
                val event = call.receive<Event>()
                val id = eventService.create(event)
                call.respond(mapOf("id" to id))
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(mapOf("error" to "Invalid ID"))
                    return@get
                }

                val event = eventService.getById(id)
                if (event == null) {
                    call.respond(mapOf("error" to "Event not found"))
                } else {
                    call.respond(event)
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(mapOf("error" to "Invalid ID"))
                    return@delete
                }

                val deleted = eventService.delete(id)
                call.respond(mapOf("deleted" to deleted))
            }
        }
    }
}
