package com.juanarton.moviecatalog.ui.fragments.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import com.bumptech.glide.Glide
import com.juanarton.core.BuildConfig
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.moviecatalog.R
import com.juanarton.moviecatalog.databinding.ActivityDetailBinding
import com.juanarton.moviecatalog.databinding.ActivityMainBinding
import com.juanarton.moviecatalog.databinding.FragmentPopularMovieBinding

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val movieData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("movieData", Movie::class.java)
        } else {
            intent.getParcelableExtra<Movie>("movieData")
        }

        movieData?.let {
            val backdropLink = buildString {
                append(BuildConfig.BASE_IMAGE_URL)
                append(movieData.backdrop_path)
            }

            val posterLink = buildString {
                append(BuildConfig.BASE_POSTER_URL)
                append(movieData.poster)
            }

            binding?.apply {
                Glide.with(this@DetailActivity)
                    .load(backdropLink)
                    .into(movieBackdrop)

                Glide.with(this@DetailActivity)
                    .load(posterLink)
                    .into(moviePoster)
            }
        }
    }
}