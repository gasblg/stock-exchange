package com.github.gasblg.routers

import com.github.gasblg.di.mainModule
import com.github.gasblg.di.schemaModule
import com.github.gasblg.models.currencies.CurrencyRequest
import com.github.gasblg.models.currencies.CurrencyResponse
import com.github.gasblg.models.items.ItemRequest

import com.github.gasblg.plugins.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
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

class CurrenciesRouteKtTest {
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
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
        val response = client.post("/items/create") {
            contentType(ContentType.Application.Json)
            setBody(
                ItemRequest(
                    "CAD/RUB",
                    1,
                    2,
                    3,
                    "currency",
                    "currency"

                )
            )
        }
        response.body<String>().let {
            assertEquals("CAD/RUB", it)
            assertEquals(HttpStatusCode.Created, response.status)
        }
    }

    @Test
    fun testCreatingCurrency() = testApplication {
        application {
            configureRouting()
            configureSerialization()
            configureHTTP()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
        val response = client.post("/currencies/create") {
            contentType(ContentType.Application.Json)
            setBody(
                CurrencyRequest("CAD/RUB", "2024-03-22", 67.479, -0.56)
            )
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun testGetCurrencies() = testApplication {
        application {
            configureRouting()
            configureSerialization()
            configureHTTP()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.get("/currencies").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testGetCurrencyByTicker() = testApplication {
        application {
            configureRouting()
            configureSerialization()
            configureHTTP()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/currencies/{ticker}") {
            contentType(ContentType.Application.Json)
            parameter(key = "ticker", value = "CAD/RUB")
        }
        response.body<CurrencyResponse>().let {
            assertEquals("CAD/RUB", it.ticker)
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

}
