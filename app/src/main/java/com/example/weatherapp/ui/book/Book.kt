package com.example.weatherapp.ui.book

data class Book(
    val title: String,
    val author: String,
    val coverUrl: String,
    val progress: Int,
    var pomodoroSessions: Int = 0,
    var isReading: Boolean = false // NOVO CAMPO
)