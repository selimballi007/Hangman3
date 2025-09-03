package org.hangman3.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.hangman3.navigation.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeScreen(gameViewModel: GameViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Theme Screen") },
                navigationIcon = {
                    Button(
                        onClick = { gameViewModel.navigateBack() },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Tema Ekranı")

            // Tema seçenekleri buraya eklenebilir
            Button(
                onClick = { /* Tema değiştirme işlemi */ }
            ) {
                Text("Koyu Tema")
            }

            Button(
                onClick = { /* Tema değiştirme işlemi */ }
            ) {
                Text("Açık Tema")
            }
        }
    }
}