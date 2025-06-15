# ---------- Stage 1: Build ----------
FROM gradle:8.4.0-jdk17 AS builder

WORKDIR /app

# Copy source code and gradle files
COPY . .

# Build the application using installDist
RUN ./gradlew installDist


# ---------- Stage 2: Run ----------
FROM openjdk:17-slim

WORKDIR /app

# Copy only the installed application (not the entire build)
COPY --from=builder /app/build/install/ktor-event-companion /app

# Expose Ktor default port
EXPOSE 8080

# Start the application
CMD ["./bin/ktor-event-companion"]
