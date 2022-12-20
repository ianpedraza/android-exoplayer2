package com.ianpedraza.exoplayerio18.ui.audio

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.IBinder
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.MimeTypes
import com.ianpedraza.exoplayerio18.R
import com.ianpedraza.exoplayerio18.ui.MainActivity
import com.ianpedraza.exoplayerio18.utils.AudioDataSource

class AudioPlayerService : Service() {

    private var player: ExoPlayer? = null
    private var playerNotificationManager: PlayerNotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        initializePlayer()

        val mediaDescriptionAdapter = object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return AudioDataSource.data[player.currentMediaItemIndex].title
            }

            override fun createCurrentContentIntent(player: Player): PendingIntent? {
                val intent = Intent(this@AudioPlayerService, MainActivity::class.java)

                return PendingIntent.getActivity(
                    this@AudioPlayerService,
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
                return BitmapFactory.decodeResource(resources, resource)
            }
        }

        val notificationListener = object : PlayerNotificationManager.NotificationListener {
            override fun onNotificationPosted(
                notificationId: Int,
                notification: Notification,
                ongoing: Boolean
            ) {
                startForeground(notificationId, notification)
            }

            override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
                stopSelf()
            }
        }

        playerNotificationManager =
            PlayerNotificationManager.Builder(this, PLAYBACK_NOTIFICATION_ID, PLAYBACK_CHANNEL_ID)
                .setChannelNameResourceId(R.string.channel_playback)
                .setChannelDescriptionResourceId(R.string.channel_playback_description)
                .setMediaDescriptionAdapter(mediaDescriptionAdapter)
                .setNotificationListener(notificationListener)
                .build()
                .also { notificationManager ->
                    notificationManager.setPlayer(player)
                }
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    private fun initializePlayer() {
        val trackSelector = DefaultTrackSelector(this).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

        player = ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()
            .also { player ->
                AudioDataSource.data.forEach { item ->
                    val adaptiveMediaItem = MediaItem.Builder()
                        .setUri(item.uri)
                        .setMimeType(MimeTypes.AUDIO_MP4)
                        .build()

                    player.addMediaItem(adaptiveMediaItem)
                }
            }

        player?.apply {
            this.playWhenReady = true
            prepare()
        }
    }

    private fun releasePlayer() {
        player?.run {
            release()
        }

        playerNotificationManager?.run {
            setPlayer(null)
        }

        player = null
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    companion object {
        private const val PLAYBACK_CHANNEL_ID = "Playback-Notification"
        private const val PLAYBACK_NOTIFICATION_ID = 734
    }
}
