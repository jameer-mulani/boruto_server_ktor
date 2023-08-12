package com.example.plugins

import com.example.models.ApiResponse
import com.example.routes.getAllHeroes
import com.example.routes.root
import com.example.routes.searchHero
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        root()
        getAllHeroes()
        searchHero()

        staticResources(remotePath = "/images", basePackage = "images")

//        static(remotePath = "/images") {
//            resources("images")
//        }
    }
}



