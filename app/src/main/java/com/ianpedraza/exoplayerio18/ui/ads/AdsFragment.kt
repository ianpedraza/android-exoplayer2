package com.ianpedraza.exoplayerio18.ui.ads

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ads.AdsMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.ianpedraza.exoplayerio18.R
import com.ianpedraza.exoplayerio18.databinding.FragmentAdsBinding
import com.ianpedraza.exoplayerio18.utils.AdsDataSource

class AdsFragment : Fragment() {

    private var _binding: FragmentAdsBinding? = null
    private val binding: FragmentAdsBinding get() = _binding!!

    private var player: ExoPlayer? = null
    private var adsLoader: ImaAdsLoader? = null

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()

        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()

        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()

        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adsLoader?.release()
    }

    private fun initializePlayer() {
        val trackSelector = DefaultTrackSelector(requireContext()).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

        player = ExoPlayer.Builder(requireContext())
            .setTrackSelector(trackSelector)
            .build()
            .also { player ->
                /**
                 * DASH -> MimeTypes.APPLICATION_MPD
                 * HLS -> MimeTypes.APPLICATION_M3U8
                 * SmoothStreaming -> MimeTypes.APPLICATION_SS
                 **/

                val adaptiveMediaItem = MediaItem.Builder()
                    .setUri(getString(R.string.media_url_dash))
                    .setMimeType(MimeTypes.APPLICATION_MPD)
                    .build()

                val defaultHttpDataSource = DefaultHttpDataSource.Factory()

                val mediaSource = DashMediaSource.Factory(defaultHttpDataSource)
                    .createMediaSource(adaptiveMediaItem)

                /*  ADS   */

                val imaSdkSettings = ImaSdkFactory.getInstance().createImaSdkSettings().apply {
                    autoPlayAdBreaks = true
                    isDebugMode = true
                }

                adsLoader = ImaAdsLoader.Builder(requireContext())
                    .setImaSdkSettings(imaSdkSettings)
                    .setMediaLoadTimeoutMs(30 * 1000)
                    .setVastLoadTimeoutMs(30 * 1000)
                    .build()
                    .apply {
                        setPlayer(player)
                    }

                val dataSpec = DataSpec(Uri.parse(AdsDataSource.SINGLE_INLINE_LINEAR))

                val adsMediaSource = AdsMediaSource(
                    mediaSource,
                    dataSpec,
                    0,
                    DefaultMediaSourceFactory(requireContext()),
                    adsLoader!!,
                    binding.playerView
                )

                binding.playerView.player = player

                player.setMediaSource(adsMediaSource)
            }

        player?.apply {
            this.playWhenReady = this@AdsFragment.playWhenReady
            seekTo(currentWindow, playbackPosition)
            prepare()
        }
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentMediaItemIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }
}
