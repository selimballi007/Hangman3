package org.hangman3.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hangman3.data.UserDataRepository
import org.hangman3.data.WordRepository
import org.hangman3.model.GameState
import org.hangman3.model.GameState.Companion.CORRECT_GUESS_SCORE
import org.hangman3.model.GameState.Companion.MIN_ATTEMPTS
import org.hangman3.model.Screen

class GameViewModel(private val userDataRepository: UserDataRepository,
               private val wordRepository: WordRepository
): ViewModel(){
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen = _currentScreen.asStateFlow()

    private val _gameState = MutableStateFlow<GameState>(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun navigateBack() {
        if (_currentScreen.value != Screen.Home) {
            _currentScreen.value = Screen.Home
        }
    }

    init {
        resetGame()
    }


    fun resetGame() {
        viewModelScope.launch {
            val wordId = userDataRepository.current_word_id_Flow.firstOrNull() ?: 0
            val score = userDataRepository.score_Flow.firstOrNull() ?: 0
            val maxStreak = userDataRepository.max_streak_Flow.firstOrNull() ?: 0

            val word = wordRepository.getWord(wordId)

            _gameState.value=GameState(
                guessedLetters = emptyList(),
                wordToGuess = word,
                score = score,
                maxStreak = maxStreak,
                sessionStreak = gameState.value.sessionStreak
            )
        }
    }

    fun guessLetter(letter: Char){
        if (gameState.value.isGameOver || gameState.value.guessedLetters.contains(letter))
            return

        val newGuessedLetters = gameState.value.guessedLetters + letter
        val isLetterCorrect = gameState.value.wordToGuess.word.contains(letter)

        if(isLetterCorrect){
            val isNowWin = gameState.value.wordToGuess.word.all { newGuessedLetters.contains(it) }
            val newScore = if(isNowWin) gameState.value.score + CORRECT_GUESS_SCORE else gameState.value.score
            val newStreak = if(isNowWin) gameState.value.sessionStreak + 1 else gameState.value.sessionStreak

            _gameState.update { currentState ->
                currentState.copy(
                    guessedLetters = newGuessedLetters,
                    isWin = isNowWin,
                    isGameOver = isNowWin,
                    score= newScore,
                    sessionStreak = newStreak
                )
            }

            if(isNowWin){
                viewModelScope.launch {
                    if((userDataRepository.max_streak_Flow.firstOrNull() ?:0) < newStreak)
                        userDataRepository.updateMaxStreak(newStreak)
                    userDataRepository.updateScore(newScore)
                    userDataRepository.updateCurrentWordId(gameState.value.wordToGuess.id + 1)
                }
            }
        }else{
            val newRemainingAttempts = gameState.value.remainingAttempts - 1
            val isNowGameOver = newRemainingAttempts <= MIN_ATTEMPTS

            _gameState.update { currentState ->
                currentState.copy(
                    guessedLetters = newGuessedLetters,
                    remainingAttempts = newRemainingAttempts,
                    isGameOver = isNowGameOver,
                    sessionStreak = if(isNowGameOver) 0 else gameState.value.sessionStreak
                )
            }
        }
    }
}