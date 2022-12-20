package com.ianpedraza.exoplayerio18.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ianpedraza.exoplayerio18.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        binding.tvHomeAds.setOnClickListener { goToAds() }
        binding.tvHomeAudio.setOnClickListener { goToAudio() }
    }

    private fun goToAds() {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAdsFragment())
    }

    private fun goToAudio() {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAudioFragment())
    }
}
