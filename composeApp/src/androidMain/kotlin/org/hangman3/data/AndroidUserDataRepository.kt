package org.hangman3.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.hangman3.data.PrefsKeys.CURRENT_WORD_ID
import org.hangman3.data.PrefsKeys.MAX_STREAK
import org.hangman3.data.PrefsKeys.SCORE
import org.hangman3.data.PrefsKeys.SELECTED_THEME_ID

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")

object PrefsKeys {
    val SCORE = intPreferencesKey("score")
    val MAX_STREAK = intPreferencesKey("max_streak")
    val CURRENT_WORD_ID = intPreferencesKey("current_word_id")
    val SELECTED_THEME_ID = stringPreferencesKey("selected_theme_id")
}

class AndroidUserDataRepository(private val context: Context) : UserDataRepository {
    override val score_Flow: Flow<Int>
        get() = context.dataStore.data.map { prefs -> prefs[SCORE] ?: 0 }

    override val max_streak_Flow: Flow<Int>
        get() = context.dataStore.data.map{ prefs -> prefs[MAX_STREAK] ?: 0 }

    override val current_word_id_Flow: Flow<Int>
        get() = context.dataStore.data.map{ prefs ->prefs[CURRENT_WORD_ID] ?: 0 }

    override val selected_theme_id_Flow: Flow<String>
        get() = context.dataStore.data.map{ prefs ->prefs[SELECTED_THEME_ID] ?: "" }


    override suspend fun updateScore(score: Int) {
        context.dataStore.edit { prefs -> prefs[SCORE] = score}
    }

    override suspend fun updateMaxStreak(streak: Int) {
        context.dataStore.edit{ prefs -> prefs[MAX_STREAK] = streak }
    }

    override suspend fun updateCurrentWordId(id: Int) {
        context.dataStore.edit{ prefs -> prefs[CURRENT_WORD_ID] = id}
    }

    override suspend fun updateSelectedThemeId(themeId: String) {
        context.dataStore.edit { prefs -> prefs[SELECTED_THEME_ID] = themeId }
    }

}