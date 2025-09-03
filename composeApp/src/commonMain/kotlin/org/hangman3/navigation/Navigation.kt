package org.hangman3.navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import org.hangman3.model.Screen
import org.hangman3.screens.GameScreen
import org.hangman3.screens.HomeScreen
import org.hangman3.screens.ThemeScreen

@Composable
fun AppNavigation(viewModel: GameViewModel) {

    val currentScreen by viewModel.currentScreen.collectAsState()

    when (currentScreen) {
        is Screen.Home -> HomeScreen(viewModel)
        is Screen.Game -> GameScreen(viewModel)
        is Screen.Theme -> ThemeScreen(viewModel)
    }
}