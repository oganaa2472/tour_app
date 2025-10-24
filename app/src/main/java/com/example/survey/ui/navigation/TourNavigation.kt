package com.example.survey.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.survey.ui.navigation.Screen
import com.example.survey.ui.screen.BookmarksScreen
import com.example.survey.ui.screen.HomeScreen
import com.example.survey.ui.screen.LoginScreen
import com.example.survey.ui.screen.ProfileScreen
import com.example.survey.ui.screen.TourDetailScreen
import com.example.survey.ui.viewmodel.AuthViewModel
import com.example.survey.ui.viewmodel.ProfileViewModel
import com.example.survey.ui.viewmodel.TourViewModel


@Composable
fun TourNavigation(
    navController: NavHostController,
    tourViewModel: TourViewModel,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onTourClick = { tourId ->
                    navController.navigate(Screen.TourDetail.createRoute(tourId))
                },
                onBookmarksClick = {
                    navController.navigate(Screen.Bookmarks.route)
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                },
                viewModel = tourViewModel
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                viewModel = authViewModel
            )
        }

        composable(
            route = Screen.TourDetail.route,
            arguments = Screen.TourDetail.arguments
        ) { backStackEntry ->
            val tourId = backStackEntry.arguments?.getString("tourId") ?: ""
            TourDetailScreen(
                tourId = tourId,
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = tourViewModel
            )
        }

        composable(Screen.Bookmarks.route) {
            BookmarksScreen(
                onTourClick = { tourId ->
                    navController.navigate(Screen.TourDetail.createRoute(tourId))
                },
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = tourViewModel
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                authViewModel = authViewModel,
                profileViewModel = profileViewModel
            )
        }
    }
}
