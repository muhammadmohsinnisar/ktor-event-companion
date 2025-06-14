# 📅 Ktor Event Companion Backend

A lightweight Kotlin-based backend service built with [Ktor](https://ktor.io/), using PostgreSQL and [Exposed](https://github.com/JetBrains/Exposed) as the ORM. This service is designed to manage events, including CRUD operations for event entities.

---

## 🚀 Features

- 🔧 REST API with Ktor
- 📦 PostgreSQL integration via Exposed ORM
- 🌐 JSON serialization with `kotlinx.serialization`
- 🧪 Simple test setup with Ktor test host
- 🧰 Coroutine-friendly database transactions

---

## 📁 Project Structure

src/
├── main/
│ ├── kotlin/
│ │ ├── com.mohsin/
│ │ │ ├── models/ # Event data model and schema
│ │ │ ├── services/ # EventService with DB access
│ │ │ └── Application.kt
│ └── resources/
│ ├── application.conf
│ └── application.yaml
---

## 🔌 API Endpoints

| Method | Endpoint         | Description                |
|--------|------------------|----------------------------|
| GET    | `/events`        | Get all events             |
| GET    | `/events/{id}`   | Get an event by ID         |
| POST   | `/events`        | Create a new event         |
| DELETE | `/events/{id}`   | Delete an event by ID      |

> ✅ Uses JSON request/response format.

---

## 🛠️ Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/your-username/ktor-event-companion.git
cd ktor-event-companion
```

### 2. Configure PostgreSQL
- Update application.yaml or application.conf with your PostgreSQL credentials:
```bash
ktor:
  deployment:
    port: 8080
  application:
    modules:
      - com.mohsin.ApplicationKt.module

  database:
    driver: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/ktor
    user: postgres
    password: your-password
```
💡 Make sure the events table exists, or create it via migration using SchemaUtils.create(Events).

### 3. Run the app
bash
```
./gradlew run
```

Server will start at: http://localhost:8080

## 🧪 Testing API with Postman

### 📌 Create event (POST)
**Endpoint:**
POST http://localhost:8080/events

**JSON Body:**
```json
{
  "name": "Tech Conference",
  "description": "A conference about the latest in tech.",
  "date": "2025-07-01",
  "location": "Berlin"
}
```

📥 Fetch all events (GET)
Endpoint:
```
GET http://localhost:8080/events
```
🔍 Fetch event by ID (GET)

Endpoint:
```
GET http://localhost:8080/events/1
```

❌ Delete event (DELETE)
Endpoint:

DELETE http://localhost:8080/events/1

🪪 License
This project is open-sourced under the MIT License. See LICENSE for details.

Let me know if you'd like this merged into your full `README.md` file or committed to the repo.








