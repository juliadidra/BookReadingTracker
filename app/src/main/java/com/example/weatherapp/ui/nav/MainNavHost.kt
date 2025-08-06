package com.example.weatherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.HomePage
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.ui.ListPage
import com.example.weatherapp.ui.MapPage
import com.example.weatherapp.ui.book.BookScreen // (a ser criada)

@Composable
fun MainNavHost(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController, startDestination = Route.Book) {

        composable<Route.Book> { BookScreen() } // Nova rota para livros
        composable<Route.Search> { BookScreen() }
        composable<Route.Profile> { BookScreen() }
    }
}