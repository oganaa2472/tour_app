package com.example.survey.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object Home : Screen("home")
    object Login : Screen("login")
    object Bookmarks : Screen("bookmarks")
    object Profile : Screen("profile") // Add this line

    object TourDetail : Screen(
        route = "tour_detail/{tourId}",
        arguments = listOf(
            navArgument("tourId") {
                type = NavType.StringType
            }
        )
    ) {
        fun createRoute(tourId: String) = "tour_detail/$tourId"
    }
}