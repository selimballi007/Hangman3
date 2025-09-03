package org.hangman3.model

data class GameState(
    val wordToGuess: WordEntry = WordEntry(_id = 0, _word = "", _explanation = ""),
    val guessedLetters: List<Char> = emptyList(),
    val remainingAttempts: Int = DEFAULT_MAX_ATTEMPTS,
    val isWin: Boolean = false,
    val isGameOver: Boolean = false,
    val score: Int = 0,
    val maxStreak: Int = 0,
    val sessionStreak: Int = 0
) {
    val displayWord: String = wordToGuess.word.map { letter ->
        if (guessedLetters.contains(letter)) letter else '_'
    }.joinToString(" ")

    companion object {
        const val DEFAULT_MAX_ATTEMPTS = 7
        const val CORRECT_GUESS_SCORE = 10
        const val MIN_ATTEMPTS = 0
    }
}

data class WordEntry(val _id: Int, val _word : String, val _explanation : String) {
    val id = _id
    val word= _word.uppercase()
    val explanation= _explanation

}