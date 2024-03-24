package com.github.gasblg.routers

import com.github.gasblg.di.mainModule
import com.github.gasblg.di.schemaModule
import com.github.gasblg.models.translations.TranslationRequest
import com.github.gasblg.models.translations.TranslationResponse
import com.github.gasblg.plugins.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import kotlin.test.*

class TranslationsRouteKtTest{

    @Before
    fun startKoinForTest() {
        startKoin {
            modules(mainModule, schemaModule)
        }
    }

    @After
    fun stopKoinAfterTest() = stopKoin()

    @Test
    fun testCreatingTranslation() = testApplication {
        application {
            configureRouting()
            configureSerialization()
            configureHTTP()
        }
        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
        val response = client.post("/translations/create") {
            contentType(ContentType.Application.Json)
            setBody(
                TranslationRequest(
                    1, "en", "text"
                )
            )
        }
        response.body<TranslationResponse>().let {
            assertEquals("text", it.text)
            assertEquals(HttpStatusCode.Created, response.status)
        }

    }

    @Test
    fun testGetTranslations() = testApplication {
        application {
            configureRouting()
            configureSerialization()
            configureHTTP()
        }
        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json()
            }
        }
        client.get("/translations").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testGetSharesById() = testApplication {
        application {
            configureRouting()
            configureSerialization()
            configureHTTP()
        }
        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json()
            }
        }

        client.get("/translations/{id}") {
            contentType(ContentType.Application.Json)
            parameter(key = "id", value = 1)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}