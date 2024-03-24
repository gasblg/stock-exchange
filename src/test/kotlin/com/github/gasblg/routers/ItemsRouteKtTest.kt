package com.github.gasblg.routers

import com.github.gasblg.di.mainModule
import com.github.gasblg.di.schemaModule
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
import kotlin.test.*
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin

class ItemsRouteKtTest {

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
                    "MTLR",
                    31,
                    32,
                    33,
                    "shares",
                    "stock"
                )
            )
        }
        response.body<String>().let {
            assertEquals("MTLR", it)
            assertEquals(HttpStatusCode.Created, response.status)
        }

    }
}