package org.hangman3.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.hangman3.model.Screen
import org.hangman3.navigation.GameViewModel

@Composable
fun HomeScreen(viewModel: GameViewModel) {

    val gameState by viewModel.gameState.collectAsState()
    var levelId = gameState.wordToGuess.id
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { viewModel.navigateTo(Screen.Game) }
        ) {
            Text( if(levelId>0) "Go to Level $levelId" else "Go to Game")
        }

        Button(
            onClick = { viewModel.navigateTo(Screen.Theme) }
        ) {
            Text("Go to Theme")
        }
    }
}