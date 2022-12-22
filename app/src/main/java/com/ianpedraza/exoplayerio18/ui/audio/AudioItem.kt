package com.ianpedraza.exoplayerio18.ui.audio

import androidx.annotation.DrawableRes

private const val EMPTY_STRING = ""

data class AudioItem(
    val id: String,
    val title: String = EMPTY_STRING,
    val description: String = EMPTY_STRING,
    val uri: String,
    @DrawableRes
    val bitmapResource: Int
) {
    override fun toString(): String = title
}
