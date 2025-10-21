package com.example.survey.presentation.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.survey.feature.login.ui.screen.LoginScreen
import com.example.survey.feature.login.data.local.TokenManager
import com.example.survey.navigation.NavRoutes

@Composable
fun NavGraph(startDestination: String? = null) {
    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    val token by tokenManager.authToken.collectAsState(initial = null)

    val initialRoute = startDestination ?: if (!token.isNullOrEmpty()) {
        NavRoutes.Home.route
    } else {
        NavRoutes.Login.route
    }

    NavHost(navController = navController, startDestination = initialRoute) {

        composable(NavRoutes.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true } // clear login from back stack
                    }
                }
            )
        }
        composable(NavRoutes.Home.route) {
            HomeScreen(
                onLogout = {
                    // Clear token and navigate back to login
//                    tokenManager.clearToken()
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // Optional: Tour detail screen
        composable(NavRoutes.TourDetail.route) { backStackEntry ->
            val tourId = backStackEntry.arguments?.getString("id") ?: ""
            // TourDetailScreen(tourId = tourId)
        }
    }
}
