package com.example.routes

import com.example.models.ApiResponse
import com.example.repository.HeroRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.getAllHeroes() {

    val heroesRepository : HeroRepository by inject<HeroRepository>()

    get("/boruto/heroes"){
        try {
            val page = call.request.queryParameters["page"]?.toInt() ?: 1

            //check the page is in 1..5 range if not then it will throw IllegalArgumentException.
            require(page in 1..5)

            //get all heroes from repository
            val allHeroes = heroesRepository.getAllHeroes(page)

            //return the 200 OK response.
            call.respond(message = allHeroes, status = HttpStatusCode.OK)

        }catch (e : NumberFormatException){
            call.respond(message = ApiResponse(success = false, message = "Page number should be number only",), status = HttpStatusCode.BadRequest)
        }catch (e : IllegalArgumentException){
            call.respond(message = ApiResponse(success = false, message = "Page number should be between 1..5 only"), status = HttpStatusCode.NotFound)
        }
    }
}