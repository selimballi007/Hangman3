package org.hangman3.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hangman3.navigation.GameViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(gameViewModel: GameViewModel) {

    val gameState by gameViewModel.gameState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Level: ${gameState.wordToGuess.id}"
                    )
                },
                navigationIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { gameViewModel.navigateBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                actions = {
                    Text(
                        text = "Score: ${gameState.score}",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        }
    )
    { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Row(
                modifier=Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                HangmanDrawing(
                    remainingAttempts = gameState.remainingAttempts,
                    modifier = Modifier
                        .weight(3f)
                        .height(200.dp)
                        .padding(20.dp)
                )
                ExplanationCard(modifier = Modifier
                    .weight(2f),
                    text = gameState.wordToGuess.explanation
                )
            }

            Spacer(modifier = Modifier
                .height(16.dp)
            )

            Text(
                text = gameState.displayWord,
                //fontFamily = getFontFamily(),
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = gameState.remainingAttempts.toString(),
                //fontFamily = getFontFamily()
            )

            DisplayKeyboard(
                onLetterClick = {letter -> gameViewModel.guessLetter(letter)},
                isDisabled = {letter -> gameState.guessedLetters.contains(letter)}
            )

            if(gameViewModel.gameState.value.isGameOver){
                EndDialog(
                    onDismissRequest = { },
                    onConfirmation = {gameViewModel.resetGame()},
                    dialogTitle = "Game Over",
                    dialogText = if(gameState.isWin) "You Win" else "You Lose"
                )
            }
        }
    }
}

    @Composable
fun EndDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String
) {
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        confirmButton = {
            TextButton( onClick = { onConfirmation()}) {
                Text("Play Again")
            }
        },
        dismissButton = {
            TextButton( onClick = {onDismissRequest()}) {
                Text("Dismiss")
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DisplayKeyboard(
    onLetterClick: (Char) -> Unit,
    isDisabled: (Char) -> Boolean
){
    FlowRow(
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center
    ) {
        ('A'..'Z').forEach { letter ->
            TextButton(
                onClick = {onLetterClick(letter)},
                enabled = !isDisabled(letter) ,
                modifier = Modifier.padding(2.dp).width(30.dp)

            ) {
                Text(
                    text = letter.uppercase(),
                    fontSize = 30.sp,
                    //fontFamily = getFontFamily(),
                    textAlign = TextAlign.Center,
                    color = if (isDisabled(letter)) Color.Gray else Color.Black
                )
            }
        }
    }
}


@Composable
fun ExplanationCard(modifier: Modifier, text : String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Text(
            text = text,
            //fontFamily = getFontFamily(),
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun HangmanDrawing(remainingAttempts: Int, modifier: Modifier ) {
    Canvas(modifier = modifier) {
        val partCount = 10 - remainingAttempts.coerceIn(0, 10)

        val topX = size.width
        val lenY = size.height
        val topY = size.height * 0.1f

        // 1. Base
        if (partCount >= 1) {
            drawLine(
                Color.Black,
                Offset(0f, size.height),
                Offset(topX, size.height),
                strokeWidth = 8f
            )
        }

        // 2. Pole
        if (partCount >= 2) {
            drawLine(
                Color.Black,
                Offset(topX / 5, lenY),
                Offset(topX / 5, topY),
                strokeWidth = 8f
            )
        }

        // 3. Beam
        if (partCount >= 3) {
            drawLine(
                Color.Black,
                Offset(topX / 5, topY),
                Offset(topX * 0.6f, topY),
                strokeWidth = 8f
            )
        }

        // 4. Rope
        if (partCount >= 4) {
            drawLine(
                Color.Black,
                Offset(topX * 0.6f, topY),
                Offset(topX * 0.6f, topY * 1.5f),
                strokeWidth = 6f
            )
        }

        // 5. Head
        if (partCount >= 5) {
            drawCircle(
                Color.Black,
                radius = topY * 0.9f,
                center = Offset(topX * 0.6f, topY * 2.4f)
            )
        }

        // 6. Body
        if (partCount >= 6) {
            drawLine(
                Color.Black,
                Offset(topX * 0.6f, topY * 3.3f),
                Offset(topX * 0.6f, topY * 6f),
                strokeWidth = 6f
            )
        }

        // 7. Left Arm
        if (partCount >= 7) {
            drawLine(
                Color.Black,
                Offset(topX * 0.6f, topY * 3.5f),
                Offset(topX * 0.45f, topY * 5f),
                strokeWidth = 5f
            )
        }

        // 8. Right Arm
        if (partCount >= 8) {
            drawLine(
                Color.Black,
                Offset(topX * 0.6f, topY * 3.5f),
                Offset(topX * 0.75f, topY * 5f),
                strokeWidth = 5f
            )
        }

        // 9. Left Leg
        if (partCount >= 9) {
            drawLine(
                Color.Black,
                Offset(topX * 0.6f, topY * 6f),
                Offset(topX * 0.45f, topY * 7.5f),
                strokeWidth = 5f
            )
        }

        // 10. Right Leg
        if (partCount >= 10) {
            drawLine(
                Color.Black,
                Offset(topX * 0.6f, topY * 6f),
                Offset(topX * 0.75f, topY * 7.5f),
                strokeWidth = 5f
            )
        }
    }
}
