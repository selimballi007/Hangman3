package org.hangman3.data

import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val score_Flow: Flow<Int>
    val max_streak_Flow: Flow<Int>
    val current_word_id_Flow: Flow<Int>
    val selected_theme_id_Flow: Flow<String>
    suspend fun updateScore(score: Int)
    suspend fun updateMaxStreak(streak: Int)
    suspend fun updateCurrentWordId(id: Int)
    suspend fun updateSelectedThemeId(id: String)
}

