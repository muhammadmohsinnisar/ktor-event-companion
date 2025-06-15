package com.mohsin.routes

import com.mohsin.models.Speaker
import com.mohsin.services.SpeakerService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.speakerRoutes() {
    val service = SpeakerService()

    route("/speakers") {
        get {
            val speakers = service.getAll()
            call.respond(speakers)
        }
    }

    route("events/{id}/speakers") {
        post {
            val speaker = call.receive<Speaker>()
            val id = service.create(speaker)
            call.respondText("Speaker created with ID $id", status = HttpStatusCode.Created)
        }
    }

    get("/events/{id}/speakers") {
        val eventId = call.parameters["id"]?.toIntOrNull()
        if (eventId == null) {
            call.respondText("Invalid event ID", status = HttpStatusCode.BadRequest)
            return@get
        }

        val speakers = service.getByEvent(eventId)
        call.respond(speakers)
    }
}
