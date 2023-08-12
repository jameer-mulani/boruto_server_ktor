package com.example.routes

import com.example.repository.HeroRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.searchHero(){

    val heroRepository by inject<HeroRepository>()

    get("/boruto/heroes/search"){
        val name = call.request.queryParameters["name"] ?: ""
        val searchedResponse = heroRepository.searchHero(name)
        call.respond(message = searchedResponse, status = HttpStatusCode.OK)
    }
}