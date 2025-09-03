package org.hangman3

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform