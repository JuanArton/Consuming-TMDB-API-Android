package com.juanarton.moviecatalog.ui.activity.detail

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.juanarton.core.BuildConfig
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.source.remote.Resource
import com.juanarton.moviecatalog.R
import com.juanarton.moviecatalog.databinding.ActivityDetailMovieBinding
import com.juanarton.moviecatalog.ui.fragments.player.PlayerFragment
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
            intent.getParcelableExtra("movieData")
        }

        val mode = intent.getStringExtra("mode")

        if (movieData != null && mode != null){
            detailMovieViewModel.setProperty(movieData.id, mode)

            binding?.playButton?.setOnClickListener {
                buildDialogBox()
            }

            val backdropLink = buildString {
                append(BuildConfig.BASE_IMAGE_URL)
                append(movieData.backdrop_path)
            }

            val posterLink = buildString {
                append(BuildConfig.BASE_POSTER_URL)
                append(movieData.poster)
            }

            binding?.apply {
                if (movieData.backdrop_path.isNullOrEmpty()){
                    playButton.visibility = View.INVISIBLE
                    watchTrailerText.text = resources.getString(R.string.noTrailer)

                    val height = 236 * this@DetailMovieActivity.resources.displayMetrics.density
                    movieBackdrop.layoutParams.height = height.toInt()

                    Glide.with(this@DetailMovieActivity)
                        .load(R.drawable.baseline_broken_image_24)
                        .into(movieBackdrop)
                } else {
                    Glide.with(this@DetailMovieActivity)
                        .load(backdropLink)
                        .into(movieBackdrop)
                }

                Glide.with(this@DetailMovieActivity)
                    .load(posterLink)
                    .into(moviePoster)

                movieTitle.text = movieData.title

                overviewContent.text = movieData.overview


                val rating = movieData.vote_average?.times(10)?.toInt()
                when(rating){
                    in 0..29 -> {
                        ratingBar.setIndicatorColor(ContextCompat.getColor(this@DetailMovieActivity, R.color.badRate))
                        ratingBar.trackColor = ContextCompat.getColor(this@DetailMovieActivity, R.color.badRateTrack)
                        ratingBar.progress = rating ?: 0
                    }
                    in 30..69 -> {
                        ratingBar.setIndicatorColor(ContextCompat.getColor(this@DetailMovieActivity, R.color.normalRate))
                        ratingBar.trackColor = ContextCompat.getColor(this@DetailMovieActivity, R.color.normalRateTrack)
                        ratingBar.progress = rating ?: 0
                    }
                    in 70..100 ->{
                        ratingBar.setIndicatorColor(ContextCompat.getColor(this@DetailMovieActivity, R.color.goodRate))
                        ratingBar.trackColor = ContextCompat.getColor(this@DetailMovieActivity, R.color.goodRateTrack)
                        ratingBar.progress = rating ?: 0
                    }
                }
                ratingPercentage.text = rating.toString()
            }
        }
    }

    private fun buildDialogBox(){
        detailMovieViewModel.movieTrailer().observe(this) { trailer ->
            when (trailer) {
                is Resource.Success ->
                    if (!trailer.data.isNullOrEmpty()) {
                        trailer.data?.let{trailerData ->
                            val itemName = List(trailerData.size) { index ->
                                val modIndex = index+1
                                "Trailer $modIndex"
                            }

                            val rowList = layoutInflater.inflate(R.layout.trailer_list_pop_up_dialog, null)
                            val trailerListView = rowList.findViewById<ListView>(R.id.listTrailer)

                            val mAdapter = ArrayAdapter(this, R.layout.trailer_item_view, R.id.text_view, itemName)
                            trailerListView.adapter = mAdapter

                            val dialog = AlertDialog.Builder(this).setView(rowList).create()

                            dialog.show()

                            val width = 350 * this.resources.displayMetrics.density
                            val layoutParams = WindowManager.LayoutParams()
                            layoutParams.copyFrom(dialog.window?.attributes)
                            layoutParams.width = width.toInt()
                            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
                            dialog.window?.attributes = layoutParams

                            trailerListView.setOnItemClickListener{ _, _, position, _ ->

                                val playerFragment = PlayerFragment()
                                val mBundle = Bundle()
                                mBundle.putString("videoID", trailerData[position].key)
                                playerFragment.arguments = mBundle

                                supportFragmentManager
                                    .beginTransaction()
                                    .replace(android.R.id.content, playerFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .commit()

                                dialog.hide()
                            }
                        }
                    } else {
                        Log.d("trailerData", "Error")
                    }

                is Resource.Loading -> Log.d("trailerData", "Loading")
                is Resource.Error -> Log.d("trailerData", "Error")
            }
        }
    }
}