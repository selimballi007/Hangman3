package org.hangman3

import androidx.compose.ui.window.ComposeUIViewController
import org.hangman3.data.IosUserDataRepository

private lateinit var repository: IosUserDataRepository

fun MainViewController() = ComposeUIViewController {
    repository= IosUserDataRepository()

    App(repository)
}