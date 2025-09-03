package org.hangman3.data


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue

class IosUserDataRepository : UserDataRepository {
    private val defaults = NSUserDefaults.Companion.standardUserDefaults

    companion object {
        private const val KEY_SCORE = "score"
        private const val KEY_MAX_STREAK = "max_streak"
        private const val KEY_CURRENT_WORD_ID = "current_word_id"
        private const val KEY_SELECTED_THEME_ID = "selected_theme_id"
    }

    // Internal state flows that keep UI in sync
    private val _scoreFlow = MutableStateFlow(defaults.integerForKey(KEY_SCORE).toInt())
    private val _maxStreakFlow = MutableStateFlow(defaults.integerForKey(KEY_MAX_STREAK).toInt())
    private val _currentWordIdFlow =
        MutableStateFlow(defaults.integerForKey(KEY_CURRENT_WORD_ID).toInt())
    private val _selectedThemeIdFlow =
        MutableStateFlow(defaults.stringForKey(KEY_SELECTED_THEME_ID).toString())

    // Exposed immutable flows
    override val score_Flow: Flow<Int> = _scoreFlow.asStateFlow()
    override val max_streak_Flow: Flow<Int> = _maxStreakFlow.asStateFlow()
    override val current_word_id_Flow: Flow<Int> = _currentWordIdFlow.asStateFlow()
    override val selected_theme_id_Flow: Flow<String> = _selectedThemeIdFlow.asStateFlow()

    override suspend fun updateScore(score: Int) {
        defaults.setInteger(score.toLong(), KEY_SCORE)
        _scoreFlow.value = score
    }

    override suspend fun updateMaxStreak(streak: Int) {
        defaults.setInteger(streak.toLong(), KEY_MAX_STREAK)
        _maxStreakFlow.value = streak
    }

    override suspend fun updateCurrentWordId(id: Int) {
        defaults.setInteger(id.toLong(), KEY_CURRENT_WORD_ID)
        _currentWordIdFlow.value = id
    }

    override suspend fun updateSelectedThemeId(id: String) {
        defaults.setObject(id.toString(), KEY_SELECTED_THEME_ID)
        _selectedThemeIdFlow.value = id.toString()
    }
}