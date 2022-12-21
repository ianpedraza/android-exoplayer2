package com.ianpedraza.exoplayerio18.ui.audio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.ianpedraza.exoplayerio18.databinding.FragmentAudioBinding

class AudioFragment : Fragment() {

    private var _binding: FragmentAudioBinding? = null
    private val binding: FragmentAudioBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        binding.btnPlay.setOnClickListener {
            startPlaying()
            // startDownloadForItem(AudioDataSource.data.first())
        }
    }

    private fun startPlaying() {
        val intent = Intent(requireContext(), AudioPlayerService::class.java)
        Util.startForegroundService(requireContext(), intent)
    }

    private fun startDownloadForItem(item: AudioItem) {
        val downloadRequest = DownloadRequest.Builder(item.id, Uri.parse(item.uri))
            .setMimeType(MimeTypes.AUDIO_MP4)
            .build()

        DownloadService.sendAddDownload(
            requireContext(),
            AudioDownloadService::class.java,
            downloadRequest,
            false
        )
    }
}
