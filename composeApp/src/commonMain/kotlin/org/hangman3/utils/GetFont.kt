package org.hangman3.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import hangman3.composeapp.generated.resources.Res
import hangman3.composeapp.generated.resources.gloriahallelujah_regular
import org.jetbrains.compose.resources.Font


@Composable
fun getGloriaFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.gloriahallelujah_regular)
    )
}
