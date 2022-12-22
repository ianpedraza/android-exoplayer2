package com.ianpedraza.exoplayerio18.ui.audio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.ianpedraza.exoplayerio18.databinding.FragmentAudioBinding
import com.ianpedraza.exoplayerio18.utils.AudioDataSource

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
        startPlaying()
    }

    private fun setupUi() {
        setupListView()
    }

    private fun setupListView() {
        val listAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            AudioDataSource.data
        )

        binding.listView.adapter = listAdapter

        binding.listView.setOnItemClickListener { _, _, index, _ ->
            val item = AudioDataSource.data[index]
            startDownloadForItem(item)
        }
    }

    private fun startPlaying() {
        val intent = Intent(requireContext(), AudioPlayerService::class.java)
        Util.startForegroundService(requireContext(), intent)
    }

    private fun startDownloadForItem(item: AudioItem) {
        val downloadRequest = DownloadRequest.Builder(item.id, Uri.parse(item.uri))
            .setData(null)
            .setCustomCacheKey(null)
            .setMimeType(MimeTypes.AUDIO_MP4)
            .build()

        DownloadService.sendAddDownload(
            requireContext(),
            AudioDownloadService::class.java,
            downloadRequest,
            false
        )

        Toast.makeText(requireContext(), "Download started", Toast.LENGTH_SHORT).show()
    }
}
