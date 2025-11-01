package ru.byksar.analclicker.simpleapi

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import ru.byksar.analclicker.pages.settings.VersionModel

class ApiClient() {
    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = true }
            )
        }
    }

    suspend fun getAppVersion(): VersionModel? {
        try {
            val response = httpClient.get("https://api.github.com/repos/jetbrains/kotlin/releases/latest")
            return response.body<VersionModel>()
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun getJoke(): JokeModel? {
        try {
            val response = httpClient.get("https://v2.jokeapi.dev/joke/Any?blacklistFlags=nsfw,sexist&type=single")
            return response.body<JokeModel>()
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun getTranslate(text: String): String? {
        try {

            val response = httpClient.get("https://translate.googleapis.com/translate_a/single") {
                parameter("client", "gtx")   // публичный клиент
                parameter("sl", "auto")      // auto detect source language
                parameter("tl", "ru")        // translate to Russian
                parameter("dt", "t")         // return only translation text
                parameter("q", text)
            }

            val json = response.body<JsonElement>()
            val translated = buildString {
                json.jsonArray.first().jsonArray.forEach { inner ->
                    append(inner.jsonArray[0].jsonPrimitive.content)
                }
            }

            return translated

        } catch (e: Exception) {
            return null
        }
    }
}
