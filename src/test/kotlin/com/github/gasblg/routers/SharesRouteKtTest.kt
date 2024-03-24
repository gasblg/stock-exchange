package com.github.gasblg.routers

import com.github.gasblg.di.mainModule
import com.github.gasblg.di.schemaModule
import com.github.gasblg.models.shares.SharesDetailResponse
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


class SharesRouteKtTest {

    @Before
    fun startKoinForTest() {
        startKoin {
            modules(mainModule, schemaModule)
        }
    }

    @After
    fun stopKoinAfterTest() = stopKoin()

    @Test
    fun testGetShares() = testApplication {
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

        client.get("/shares").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }


    @Test
    fun testGetSharesByTicker() = testApplication {
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

        val response = client.get("/shares/{ticker}") {
            contentType(ContentType.Application.Json)
            parameter(key = "ticker", value = "MTLR")
        }
        response.body<SharesDetailResponse>().let {
            assertEquals("MTLR", it.ticker)
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }


}