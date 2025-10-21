package com.example.survey.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.survey.R
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Home", Icons.Filled.Home)
    object Bookings : BottomNavItem("bookings", "Bookings", Icons.Filled.Book)
    object Profile : BottomNavItem("profile", "Profile", Icons.Filled.Person)
}