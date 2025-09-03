package org.hangman3

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.hangman3.data.UserDataRepository
import org.hangman3.data.WordRepository
import org.hangman3.navigation.AppNavigation
import org.hangman3.navigation.GameViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App( userDataRepository: UserDataRepository) {
    MaterialTheme {
        val wordRepository = remember { WordRepository() }

        val gameViewModel = GameViewModel(userDataRepository, wordRepository)

        val model : GameViewModel = remember { gameViewModel }

        AppNavigation(model)
    }
}