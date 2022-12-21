package com.ianpedraza.exoplayerio18.ui.audio

import android.content.Context
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.ianpedraza.exoplayerio18.utils.AudioDataSource

class DefaultTimelineQueueNavigator(
    private val context: Context,
    private val mediaSession: MediaSessionCompat
) : TimelineQueueNavigator(mediaSession) {
    override fun getMediaDescription(
        player: Player,
        windowIndex: Int
    ): MediaDescriptionCompat {
        return AudioDataSource.getMediaDescription(
            context,
            AudioDataSource.data[windowIndex]
        )
    }
}
