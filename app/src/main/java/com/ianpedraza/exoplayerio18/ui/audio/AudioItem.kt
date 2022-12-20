package com.ianpedraza.exoplayerio18.ui.audio

private const val EMPTY_STRING = ""

data class AudioItem(
    val title: String = EMPTY_STRING,
    val description: String = EMPTY_STRING,
    val uri: String,
    val bitmapResource: Int
)
