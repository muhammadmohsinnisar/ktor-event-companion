package com.mohsin.routes

import com.mohsin.models.Event
import com.mohsin.services.EventService
import com.mohsin.utils.QrCodeGenerator
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.eventRoutes() {
    val service = EventService()

    route("/events") {
        get {
            val events = service.getAll()
            call.respond(events)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
                return@get
            }

            val event = service.getById(id)
            if (event == null) {
                call.respondText("Event not found", status = HttpStatusCode.NotFound)
            } else {
                call.respond(event)
            }
        }

        get("/{id}/qr") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
                return@get
            }

            val event = service.getById(id)
            if (event == null) {
                call.respondText("Event not found", status = HttpStatusCode.NotFound)
                return@get
            }

            val json = Json.encodeToString(Event.serializer(), event)
            val qrCodeBytes = QrCodeGenerator.generateQrCode(json)

            call.respondBytes(qrCodeBytes, contentType = ContentType.Image.PNG)
        }

        post {
            val event = call.receive<Event>()
            val newId = service.create(event)
            call.respondText("Event created with ID $newId", status = HttpStatusCode.Created)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
                return@delete
            }

            val deleted = service.delete(id)
            if (deleted) {
                call.respondText("Deleted")
            } else {
                call.respondText("Event not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}
