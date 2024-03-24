package com.github.gasblg.routers

import com.github.gasblg.di.mainModule
import com.github.gasblg.di.schemaModule
import com.github.gasblg.models.candles.CandleRequest
import com.github.gasblg.models.candles.CandleResponse
import com.github.gasblg.plugins.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.junit.After
import org.junit.Before
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import kotlin.test.*

class CandlesRouteKtTest {

    @Before
    fun startKoinForTest() {
        startKoin {
            modules(mainModule, schemaModule)
        }
    }

    @After
    fun stopKoinAfterTest() = stopKoin()


    @Test
    fun testCreatingCandle() = testApplication {
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
        val response = client.post("/candles/create") {
            contentType(ContentType.Application.Json)
            setBody(
                CandleRequest(
                    "MTLR", "2024-02-06", 287.6, 276.24, 288.9, 279.62, 7931445, 279.62, -2.74
                )
            )
        }
        response.body<CandleResponse>().let {
            assertEquals(
                CandleResponse(
                    "MTLR", "2024-02-06", 287.6, 276.24, 288.9, 279.62, 7931445, 279.62, -2.74
                ), it
            )
            assertEquals(HttpStatusCode.Created, response.status)
        }

    }

    @Test
    fun testGetCandles() = testApplication {
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

        client.get("/candles").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}