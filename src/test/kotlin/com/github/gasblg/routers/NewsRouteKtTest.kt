package com.github.gasblg.routers

import com.github.gasblg.di.mainModule
import com.github.gasblg.di.schemaModule
import com.github.gasblg.models.news.NewsDetailResponse
import com.github.gasblg.models.news.NewsRequest
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

class NewsRouteKtTest {
    @Before
    fun startKoinForTest() {
        startKoin {
            modules(mainModule, schemaModule)
        }
    }

    @After
    fun stopKoinAfterTest() = stopKoin()


    @Test
    fun testCreatingNews() = testApplication {
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
        val response = client.post("/news/create") {
            contentType(ContentType.Application.Json)
            setBody(
                NewsRequest(1, "2024", 1)
            )
        }
        response.body<Int>().let {
            assertEquals(HttpStatusCode.Created, response.status)
        }
    }

    @Test
    fun testGetNews() = testApplication {
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

        client.get("/news").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testGetNewsById() = testApplication {
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

        val response = client.get("/news/{id}") {
            contentType(ContentType.Application.Json)
            parameter(key = "id", value = 1)
        }
        response.body<NewsDetailResponse>().let {
            assertEquals(1, it.id)

            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

}