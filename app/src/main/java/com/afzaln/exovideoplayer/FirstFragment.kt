package com.afzaln.exovideoplayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import com.afzaln.exovideoplayer.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val viewModel: VideoViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val player = ExoPlayer.Builder(requireContext()).build().apply {
            volume = 0f
            setVideoTextureView(binding.playerView)
            repeatMode = Player.REPEAT_MODE_ALL
            addListener(
                object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)
                        binding.playerView.isVisible = isPlaying
                        if (isPlaying) {
                            binding.playerView.animate().alpha(1f).duration = 100

                        } else {
                            binding.playerView.alpha = 0f

                        }
                    }

                    override fun onVideoSizeChanged(videoSize: VideoSize) {
                        super.onVideoSizeChanged(videoSize)
                        binding.playerView.onVideoSizeChanged(videoSize.width, videoSize.height)
                    }
                },
            )
        }

        viewModel.videoUri.observe(viewLifecycleOwner) { videoUrl ->
            val mediaItem = MediaItem.fromUri(videoUrl)

            player.stop()
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()
        }

        binding.play.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
