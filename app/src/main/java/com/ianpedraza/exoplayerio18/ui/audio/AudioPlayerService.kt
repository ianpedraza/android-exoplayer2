package com.ianpedraza.exoplayerio18.ui.audio

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.ianpedraza.exoplayerio18.R
import com.ianpedraza.exoplayerio18.utils.AudioDataSource
import com.ianpedraza.exoplayerio18.utils.DownloadUtil

class AudioPlayerService : Service() {

    private var player: ExoPlayer? = null
    private var playerNotificationManager: PlayerNotificationManager? = null
    private var mediaSession: MediaSessionCompat? = null
    private var mediaSessionConnector: MediaSessionConnector? = null

    override fun onCreate() {
        super.onCreate()
        initializePlayer()

        val mediaDescriptionAdapter = MediaDescriptorAdapter(this)
        val notificationListener = NotificationListener(this)

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

        mediaSession = MediaSessionCompat(this@AudioPlayerService, MEDIA_SESSION_TAG)
            .also { mediaSession ->
                mediaSession.isActive = true
                playerNotificationManager?.setMediaSessionToken(mediaSession.sessionToken)
            }

        val timelineQueueNavigator = DefaultTimelineQueueNavigator(this, mediaSession!!)

        mediaSessionConnector = MediaSessionConnector(mediaSession!!)
            .also { mediaSessionConnector ->
                mediaSessionConnector.setQueueNavigator(timelineQueueNavigator)
                mediaSessionConnector.setPlayer(player)
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

        val dataSourceFactory = DefaultHttpDataSource.Factory()

        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(DownloadUtil.getCache(this))
            .setUpstreamDataSourceFactory(dataSourceFactory)

        val concatenatingMediaSource = ConcatenatingMediaSource()

        player = ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()
            .also { player ->
                AudioDataSource.data.forEach { item ->
                    val mediaItem = MediaItem.Builder()
                        .setUri(item.uri)
                        .setMimeType(MimeTypes.AUDIO_MP4)
                        .build()

                    val mediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                        .createMediaSource(mediaItem)

                    // player.addMediaItem(mediaItem)
                    concatenatingMediaSource.addMediaSource(mediaSource)
                }

                player.setMediaSource(concatenatingMediaSource)
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

        mediaSession?.run {
            release()
        }

        mediaSessionConnector?.run {
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
        private const val MEDIA_SESSION_TAG = "MediaSessionAudioPlayback"
    }
}
