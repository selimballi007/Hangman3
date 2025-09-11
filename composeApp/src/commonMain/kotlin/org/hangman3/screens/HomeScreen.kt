package org.hangman3.screens

import NotebookBackground
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hangman3.composeapp.generated.resources.Res
import hangman3.composeapp.generated.resources.button
import hangman3.composeapp.generated.resources.level
import hangman3.composeapp.generated.resources.watch_and_gain
import org.hangman3.ads.provideRewardedAdService
import org.hangman3.model.Screen
import org.hangman3.navigation.GameViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(viewModel: GameViewModel) {

    val rewardedAdService = provideRewardedAdService()
    rewardedAdService.loadRewardedAd()

    val gameState by viewModel.gameState.collectAsState()
    val levelId = gameState.wordToGuess.id
    Box(modifier = Modifier.fillMaxSize()) {
        NotebookBackground(
            modifier = Modifier.fillMaxSize()
        )
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = Color.Unspecified
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SketchButton(stringResource(Res.string.level) + " $levelId",
                    { viewModel.navigateTo(Screen.Game) }
                )

                Spacer(
                    modifier = Modifier
                        .height(40.dp)
                )

                SketchButton(stringResource(Res.string.watch_and_gain),
                    { rewardedAdService.showRewardedAd {
                        viewModel.onExtraAttempt()
                    } }
                )
                
                Image(
                    painter = painterResource(Res.drawable.button),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}


