package com.mohsin.routes

import com.mohsin.models.Event
import com.mohsin.services.EventService
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.eventRoutes() {
    val service = EventService()

    route("/events") {
        get {
            call.respond(service.getAll())
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid ID", status = io.ktor.http.HttpStatusCode.BadRequest)
                return@get
            }
            val event = service.getById(id)
            if (event == null) {
                call.respondText("Event not found", status = io.ktor.http.HttpStatusCode.NotFound)
            } else {
                call.respond(event)
            }
        }
        post {
            val event = call.receive<Event>()
            val newId = service.create(event)
            call.respondText("Event created with ID $newId")
        }
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid ID", status = io.ktor.http.HttpStatusCode.BadRequest)
                return@delete
            }
            val deleted = service.delete(id)
            if (deleted) {
                call.respondText("Deleted")
            } else {
                call.respondText("Event not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
        }
    }
}
