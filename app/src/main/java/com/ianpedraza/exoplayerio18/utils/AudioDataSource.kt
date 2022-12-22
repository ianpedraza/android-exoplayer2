package com.ianpedraza.exoplayerio18.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.annotation.DrawableRes
import com.ianpedraza.exoplayerio18.R
import com.ianpedraza.exoplayerio18.ui.audio.AudioItem
import java.util.UUID

object AudioDataSource {

    fun getMediaDescription(context: Context, item: AudioItem): MediaDescriptionCompat {
        val bitmap = getBitmap(context, item.bitmapResource)
        val extras = Bundle().apply {
            putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
            putParcelable(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, bitmap)
        }

        return item.run {
            MediaDescriptionCompat.Builder()
                .setMediaId(id)
                .setIconBitmap(bitmap)
                .setTitle(title)
                .setDescription(description)
                .setExtras(extras)
                .build()
        }
    }

    fun getBitmap(context: Context, @DrawableRes bitmapResource: Int): Bitmap {
        return BitmapFactory.decodeResource(context.resources, bitmapResource)
    }

    val data = arrayListOf(
        AudioItem(
            id = UUID.randomUUID().toString(),
            title = "Jazz in Paris",
            description = "Jazz for the masses",
            uri = "https://storage.googleapis.com/exoplayer-test-media-0/Jazz_In_Paris.mp3",
            bitmapResource = R.drawable.album_art_1
        ),
        AudioItem(
            id = UUID.randomUUID().toString(),
            title = "The messenger",
            description = "Hipster guide to London",
            uri = "https://storage.googleapis.com/automotive-media/The_Messenger.mp3",
            bitmapResource = R.drawable.album_art_2
        ),
        AudioItem(
            id = UUID.randomUUID().toString(),
            title = "Talkies",
            description = "If it talks like a duck and walks like a duck.",
            uri = "https://storage.googleapis.com/automotive-media/Talkies.mp3",
            bitmapResource = R.drawable.album_art_3
        )
    )
}
