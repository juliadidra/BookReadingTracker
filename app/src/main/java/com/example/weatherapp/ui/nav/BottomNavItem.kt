package com.example.weatherapp.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Home : Route
    @Serializable
    data object Profile : Route
    @Serializable
    data object Search : Route
    @Serializable
    data object Pomodoro : Route
    
}
sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: Route)
{
    data object HomeButton :
        BottomNavItem("Início", Icons.Default.Home, Route.Home)
    data object SearchButton :
        BottomNavItem("Buscar", Icons.Default.Search, Route.Search)
    data object ProfileButton :
        BottomNavItem("Perfil", Icons.Default.Person, Route.Profile)
    data object PomodoroButton :
        BottomNavItem("Timer", Icons.Default.Timer, Route.Pomodoro)
}