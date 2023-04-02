package me.obsilabor.torch

import com.github.ajalt.mordant.terminal.Terminal
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json

val terminal = Terminal()

val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    isLenient = true
}

lateinit var mainScope: CoroutineScope

val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(json)
    }
}

fun tab(): String { // TODO add config for indents
    return "    "
}