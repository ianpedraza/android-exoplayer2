package com.ianpedraza.exoplayerio18.ui.audio

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.ianpedraza.exoplayerio18.ui.MainActivity
import com.ianpedraza.exoplayerio18.utils.AudioDataSource

class MediaDescriptorAdapter(private val context: Context) :
    PlayerNotificationManager.MediaDescriptionAdapter {

    override fun getCurrentContentTitle(player: Player): CharSequence {
        return AudioDataSource.data[player.currentMediaItemIndex].title
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        val intent = Intent(context, MainActivity::class.java)

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun getCurrentContentText(player: Player): CharSequence {
        return AudioDataSource.data[player.currentMediaItemIndex].description
    }

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap {
        val resource = AudioDataSource.data[player.currentMediaItemIndex].bitmapResource
        return AudioDataSource.getBitmap(context, resource)
    }
}
