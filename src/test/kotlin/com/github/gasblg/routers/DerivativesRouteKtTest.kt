package com.github.gasblg.routers

import com.github.gasblg.di.mainModule
import com.github.gasblg.di.schemaModule
import com.github.gasblg.models.derivatives.DerivativesRequest
import com.github.gasblg.models.derivatives.DerivativesResponse
import com.github.gasblg.models.items.ItemRequest
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

class DerivativesRouteKtTest {
    @Before
    fun startKoinForTest() {
        startKoin {
            modules(mainModule, schemaModule)
        }
    }

    @After
    fun stopKoinAfterTest() = stopKoin()

    @Test
    fun testCreatingItem() = testApplication {
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
        val response = client.post("/items/create") {
            contentType(ContentType.Application.Json)
            setBody(
                ItemRequest(
                    ticker = "RGBI",
                    34,
                    35,
                    36,
                    "futures",
                    "derivatives"
                )
            )
        }
        response.body<String>().let {
            assertEquals("RGBI", it)
            assertEquals(HttpStatusCode.Created, response.status)
        }

    }

    @Test
    fun testCreatingDerivative() = testApplication {
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
        val response = client.post("/derivatives/create") {
            contentType(ContentType.Application.Json)
            setBody(
                DerivativesRequest(
                    ticker = "RGBI",
                    firstTrade = "2023-11-20",
                    lastTrade = "2024-06-03",
                    date = "2024-06-03",
                    contractTypeId = 37
                )
            )
        }

        response.body<String>().let {
            assertEquals("RGBI", it)
            assertEquals(HttpStatusCode.Created, response.status)
        }
    }

    @Test
    fun testGetDerivatives() = testApplication {
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
        client.get("/derivatives").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }


    @Test
    fun testGetDerivativesByTicker() = testApplication {
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

        val response = client.get("/derivatives/{ticker}") {
            contentType(ContentType.Application.Json)
            parameter(key = "ticker", value = "RGBI")
        }
        response.body<DerivativesResponse>().let {
            assertEquals("RGBI", it.ticker)
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }
}