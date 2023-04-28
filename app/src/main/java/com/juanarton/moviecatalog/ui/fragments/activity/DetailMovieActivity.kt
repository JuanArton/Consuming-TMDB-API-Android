package com.juanarton.moviecatalog.ui.fragments.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.juanarton.core.BuildConfig
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.source.remote.Resource
import com.juanarton.moviecatalog.databinding.ActivityDetailMovieBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailMovieActivity : AppCompatActivity() {

    private var _binding: ActivityDetailMovieBinding? = null
    private val binding get() = _binding
    private val detailMovieViewModel: DetailMovieViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val movieData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("movieData", Movie::class.java)
        } else {
            intent.getParcelableExtra<Movie>("movieData")
        }

        movieData?.let {
            detailMovieViewModel.setID(it.id)
            detailMovieViewModel.movieTrailer().observe(this) { trailer ->
                when (trailer) {
                    is Resource.Success ->
                        if (!trailer.data.isNullOrEmpty()) {
                            trailer.data?.let{ trailerList ->
                                binding?.trailerPlayer?.let { youTubePlayerView ->
                                    lifecycle.addObserver(youTubePlayerView)

                                    val y = IFramePlayerOptions.Builder()
                                        .controls(1)
                                        .fullscreen(1)
                                        .build()

                                    youTubePlayerView.initialize(object : AbstractYouTubePlayerListener(){
                                        override fun onReady(youTubePlayer: YouTubePlayer) {
                                            super.onReady(youTubePlayer)
                                            youTubePlayer.cueVideo(trailerList[0].key, 0F)
                                        }
                                    }, false, y)

                                }
                            }
                        } else {
                            Log.d("trailerData", "Error")
                        }

                    is Resource.Loading -> Log.d("trailerData", "Loading")
                    is Resource.Error -> Log.d("trailerData", "Error")
                }
            }

            val posterLink = buildString {
                append(BuildConfig.BASE_POSTER_URL)
                append(movieData.poster)
            }

            binding?.apply {
                Glide.with(this@DetailMovieActivity)
                    .load(posterLink)
                    .into(moviePoster)
            }
        }
    }
}