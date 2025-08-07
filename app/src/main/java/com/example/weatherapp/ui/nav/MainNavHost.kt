package com.example.weatherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.HomePage
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.pomodoro.PomodoroScreen
import com.example.weatherapp.ui.ListPage
import com.example.weatherapp.ui.MapPage
import com.example.weatherapp.ui.book.BookScreen // (a ser criada)
import com.example.weatherapp.ui.book.BookSearchScreen
import com.example.weatherapp.ui.book.ProfileScreen

@Composable
fun MainNavHost(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController, startDestination = Route.Home) {

        composable<Route.Home> { BookScreen(mainViewModel = viewModel) } // Nova rota para livros
        composable<Route.Search> { BookSearchScreen(mainViewModel = viewModel) }
        composable<Route.Profile> { ProfileScreen() }
        composable<Route.Pomodoro> { PomodoroScreen(mainViewModel = viewModel) }
    }
}