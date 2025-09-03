package org.hangman3.model

sealed class Screen {
    object Home : Screen()
    object Game : Screen()
    object Theme : Screen()
}