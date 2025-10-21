package com.example.survey.navigation

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object Login : NavRoutes("login")
    object Home : NavRoutes("home")
    object TourDetail : NavRoutes("tour_detail/{id}") {
        fun createRoute(id: String) = "tour_detail/$id"
    }
}