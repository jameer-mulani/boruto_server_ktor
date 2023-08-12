package com.example

import com.example.models.ApiResponse
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testRoot(){
        testApplication {
            val response = client.get("/")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("Welcome to Boruto API!", response.bodyAsText())
        }
    }

    @Test
    fun `test AllHeroes route without page param`(){
        testApplication {
            val response = client.get("/boruto/heroes")
            assertEquals(expected = HttpStatusCode.OK, actual = response.status)
//            assertEquals(expected = true, actual = (response.body() as ApiResponse).heroes.isNotEmpty())
        }
    }

    @Test
    fun `test AllHeroes route with page param between value 1 to 5`(){
        testApplication {
            val response = client.get("/boruto/heroes")
            assertEquals(expected = HttpStatusCode.OK, actual = response.status)

            val response1 = client.get("/boruto/heroes?page=1")
            assertEquals(expected = HttpStatusCode.OK, actual = response1.status)

            val response2 = client.get("/boruto/heroes?page=2")
            assertEquals(expected = HttpStatusCode.OK, actual = response2.status)

            val response3 = client.get("/boruto/heroes?page=3")
            assertEquals(expected = HttpStatusCode.OK, actual = response3.status)

            val response4 = client.get("/boruto/heroes?page=4")
            assertEquals(expected = HttpStatusCode.OK, actual = response4.status)

            val response5 = client.get("/boruto/heroes?page=5")
            assertEquals(expected = HttpStatusCode.OK, actual = response5.status)
        }
    }

    @Test
    fun `test AllHeroes route when page param value less than 1`()= testApplication {
        val response = client.get("/boruto/heroes?page=0")
        assertEquals(expected = HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `test AllHeroes route when page param value is not number`()= testApplication {
        val response = client.get("/boruto/heroes?page=abc")
        assertEquals(expected = HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `test AllHeroes route when page param is greater than 5`()= testApplication {
        val response = client.get("/boruto/heroes?page=6")
        assertEquals(expected = HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `when page index 1 then prevPage in AllPageRoute should be null`() = testApplication {
        val response = client.get("/boruto/heroes?page=1")
        val apiResponse = Json.decodeFromString<ApiResponse>(response.bodyAsText())
        assertEquals(true, apiResponse.prevPage == null)
    }

    @Test
    fun `when page index 5 then nextPage in AllPageRoute should be null`() = testApplication {
        val response = client.get("/boruto/heroes?page=5")
        val apiResponse = Json.decodeFromString<ApiResponse>(response.bodyAsText())
        assertEquals(true, apiResponse.nextPage == null)
    }

    @Test
    fun `when page index is in valida range then nextPage & prevPage in AllPageRoute should be greater and less than one respectively`() = testApplication {

        val givenIndex = 3
        val expectedPrevPageIndex = 2
        val expectedNextPageIndex = 4

        val response = client.get("/boruto/heroes?page=$givenIndex")
        val apiResponse = Json.decodeFromString<ApiResponse>(response.bodyAsText())
        assert(apiResponse.prevPage == expectedPrevPageIndex)
        assert(apiResponse.nextPage == expectedNextPageIndex)
    }

    @Test
    fun `when no search value then SearchHero route should return empty list`()= testApplication {
        val response = client.get("/boruto/heroes/search")
        val resultApiResponse = Json.decodeFromString<ApiResponse>(response.bodyAsText())
        assertEquals(expected = true, actual = resultApiResponse.heroes.isEmpty())
    }

    @Test
    fun `when valid search key given then SearchHero route should return at least one result`() = testApplication {
        val response = client.get("/boruto/heroes/search?name=boruto")
        assertEquals(expected = HttpStatusCode.OK, response.status)

        val resultApiResponse = Json.decodeFromString<ApiResponse>(response.bodyAsText())
        assertEquals(expected = true, resultApiResponse.heroes.isNotEmpty())

    }

    @Test
    fun `when invalid search key given then SearchHero route should return zero results`()= testApplication {
        val response = client.get("/boruto/heroes/search?name=cafebabe")
        assertEquals(expected = HttpStatusCode.OK, response.status)

        val resultApiResponse = Json.decodeFromString<ApiResponse>(response.bodyAsText())
        assert(resultApiResponse.heroes.isEmpty())
    }

    @Test
    fun `when invalid route then status code 404 should return`()= testApplication {
        val response = client.get("/boruto/login")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }


}