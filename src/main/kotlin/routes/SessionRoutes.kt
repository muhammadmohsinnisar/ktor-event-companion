package com.mohsin.routes

import com.mohsin.models.Session
import com.mohsin.models.SessionCreateRequest
import com.mohsin.services.SessionService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.sessionRoutes() {
    val service = SessionService()

    route("/events/{eventId}/sessions") {
        get {
            val eventId = call.parameters["eventId"]?.toIntOrNull()
            if (eventId == null) {
                call.respondText("Invalid event ID", status = HttpStatusCode.BadRequest)
                return@get
            }

            val sessions = service.getByEventWithSpeakers(eventId)
            call.respond(sessions)
        }

        post {
            val eventId = call.parameters["eventId"]?.toIntOrNull()
            if (eventId == null) {
                call.respondText("Invalid event ID", status = HttpStatusCode.BadRequest)
                return@post
            }

            val input = call.receive<SessionCreateRequest>()
            val session = Session(
                id = 0,
                title = input.title,
                description = input.description,
                startTime = input.startTime,
                endTime = input.endTime,
                eventId = eventId,
                speakerId = input.speakerId
            )

            val newId = service.create(session)
            call.respondText("Session created with ID $newId")
        }

    }

    route("/sessions") {
        get {
            val sessions = service.getAllSessions()
            call.respond(sessions)
        }
    }
}
