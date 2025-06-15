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

            val event = service.getEventWithDetails(id)
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

            // 👇 Use deep link instead of raw JSON
            val baseUrl = System.getenv("PUBLIC_BASE_URL") ?: "http://localhost:8080"
            val url = "$baseUrl/events/$id/details"
            val qrCodeBytes = QrCodeGenerator.generateQrCode(url)

            call.respondBytes(qrCodeBytes, contentType = ContentType.Image.PNG)
        }

        get("/{id}/details") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
                return@get
            }

            val event = service.getEventWithDetails(id)
            if (event == null) {
                call.respondText("Event not found", status = HttpStatusCode.NotFound)
                return@get
            }

            call.respondText(
                """
        <html>
            <head><title>${event.name}</title></head>
            <body>
                <h1>${event.name}</h1>
                <p><strong>Date:</strong> ${event.date}</p>
                <p><strong>Location:</strong> ${event.location}</p>
                <p><strong>Description:</strong> ${event.description}</p>

                <h2>Sessions:</h2>
                <ul>
                    ${event.sessions.joinToString("") {
                    """
                        <li>
                            <strong>${it.title}</strong><br>
                            ${it.startTime} – ${it.endTime}<br>
                            <em>${it.description}</em><br>
                            <strong>Speaker:</strong> ${it.speaker.name} (${it.speaker.bio})
                        </li><br>
                        """.trimIndent()
                }}
                </ul>
            </body>
        </html>
        """.trimIndent(), ContentType.Text.Html
            )
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
