package com.juanarton.moviecatalog.ui.fragments.player

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.juanarton.moviecatalog.R
import com.juanarton.moviecatalog.databinding.FragmentPlayerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding?.fragmentTrailerPlayer?.let {
            WindowInsetsControllerCompat(requireActivity()
                .window, it).let {controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat
                    .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }


        val bundle = arguments
        bundle?.getString("videoID")?.let { videoID ->
            binding?.youtubePlayerView?.let { videoPlayer ->
                lifecycle.addObserver(videoPlayer)

                val fullscreenViewContainer = view.findViewById<FrameLayout>(R.id.full_screen_view_container)

                val iFrameOption = IFramePlayerOptions.Builder()
                    .controls(1)
                    .fullscreen(1)
                    .build()
                videoPlayer.addFullscreenListener(object :FullscreenListener{
                    override fun onEnterFullscreen(
                        fullscreenView: View,
                        exitFullscreen: () -> Unit
                    ) {
                        videoPlayer.visibility = View.GONE
                        fullscreenViewContainer.visibility = View.VISIBLE
                        fullscreenViewContainer.addView(fullscreenView)
                        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                    }

                    override fun onExitFullscreen() {
                        videoPlayer.visibility = View.VISIBLE
                        fullscreenViewContainer.visibility = View.GONE
                        fullscreenViewContainer.removeAllViews()
                        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    }
                })
                videoPlayer.initialize(object : AbstractYouTubePlayerListener(){
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        super.onReady(youTubePlayer)
                        youTubePlayer.loadVideo(videoID, 0F)
                        youTubePlayer.toggleFullscreen()
                    }
                }, false, iFrameOption)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(
            this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    binding?.fragmentTrailerPlayer?.let {
                        WindowInsetsControllerCompat(requireActivity()
                        .window, it).show(WindowInsetsCompat.Type.systemBars())
                    }

                    activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.remove(this@PlayerFragment)
                        ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        ?.commit()
                }
            })
    }
}