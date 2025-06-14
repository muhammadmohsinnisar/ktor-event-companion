plugins {
    alias(libs.plugins.kotlin.jvm)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
    application
}

group = "com.mohsin"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.postgresql)

    implementation(libs.logback.classic)

    implementation(libs.kotlin.asyncapi.ktor)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.config.yaml)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
