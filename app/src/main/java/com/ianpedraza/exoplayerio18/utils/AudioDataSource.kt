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
            title = "Sample Track 1",
            description = "Sample Album",
            uri = "https://storage.googleapis.com/exoplayer-test-media-0/Jazz_In_Paris.mp3",
            bitmapResource = R.drawable.android_cover
        ),
        AudioItem(
            id = UUID.randomUUID().toString(),
            title = "Sample Track 2",
            description = "Sample Album",
            uri = "https://storage.googleapis.com/exoplayer-test-media-0/Jazz_In_Paris.mp3",
            bitmapResource = R.drawable.android_cover
        ),
        AudioItem(
            id = UUID.randomUUID().toString(),
            title = "Sample Track 3",
            description = "Sample Album",
            uri = "https://storage.googleapis.com/exoplayer-test-media-0/Jazz_In_Paris.mp3",
            bitmapResource = R.drawable.android_cover
        ),
        AudioItem(
            id = UUID.randomUUID().toString(),
            title = "Sample Track 4",
            description = "Sample Album",
            uri = "https://storage.googleapis.com/exoplayer-test-media-0/Jazz_In_Paris.mp3",
            bitmapResource = R.drawable.android_cover
        ),
        AudioItem(
            id = UUID.randomUUID().toString(),
            title = "Sample Track 5",
            description = "Sample Album",
            uri = "https://storage.googleapis.com/exoplayer-test-media-0/Jazz_In_Paris.mp3",
            bitmapResource = R.drawable.android_cover
        )
    )
}
