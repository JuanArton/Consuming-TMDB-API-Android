package com.juanarton.moviecatalog.ui.activity.detail

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.juanarton.core.BuildConfig
import com.juanarton.core.adapter.TrailerListAdapter
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.data.source.remote.Resource
import com.juanarton.moviecatalog.R
import com.juanarton.moviecatalog.databinding.ActivityDetailMovieBinding
import com.juanarton.moviecatalog.utils.DataHolder
import jp.wasabeef.glide.transformations.BlurTransformation
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailMovieActivity : AppCompatActivity() {

    private var _binding: ActivityDetailMovieBinding? = null
    private val binding get() = _binding
    private val detailMovieViewModel: DetailMovieViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val movieData = DataHolder.movie

        val trailerItemList: MutableList<String> = mutableListOf()
        val trailerList: MutableList<Trailer> = mutableListOf()

        movieData?.let {movie ->
            detailMovieViewModel.setProperty(movie)
            detailMovieViewModel.checkFavorite()

            setUI(movie)

            binding?.apply {

                detailMovieViewModel.isFav.observe(this@DetailMovieActivity){ favStat ->
                    when (favStat) {
                        false -> {
                            /*btFavorite.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@DetailMovieActivity, R.drawable.baseline_favorite_border_24
                                )
                            )
                            detailMovieViewModel.setFav(favStat)*/
                        }
                        true -> {
                            /*btFavorite.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@DetailMovieActivity, R.drawable.baseline_favorite_24
                                )
                            )*/
                            detailMovieViewModel.setFav(favStat)
                        }
                    }
                }

                detailMovieViewModel.movieTrailer().observe(this@DetailMovieActivity){ trailer ->
                    when(trailer){
                        is Resource.Success ->
                            if (!trailer.data.isNullOrEmpty()) {
                                trailer.data?.let { trailerData ->
                                    if (trailerData.isNotEmpty()){
                                        tvLoading.isVisible = false
                                        lottieTrailerLoading.isVisible = false
                                    }
                                    trailerList.addAll(trailerData)
                                    trailerItemList.addAll(
                                        List(trailerData.size) { index ->
                                            val modIndex = index + 1
                                            "Trailer $modIndex"
                                        }
                                    )
                                }
                            } else {
                                Log.d("trailerData", "emptyData")
                                tvLoading.text = resources.getString(R.string.noTrailer)
                                lottieTrailerLoading.isVisible = false
                            }
                        is Resource.Loading -> Log.d("trailerData", "Loading")
                        is Resource.Error -> {
                            if (trailer.message.isNullOrEmpty()){
                                tvLoading.text = resources.getString(R.string.noTrailer)
                            } else {
                                tvLoading.text = resources.getString(R.string.failedFetchData)
                            }
                            lottieTrailerLoading.isVisible = false
                            Log.d("trailerData1", trailer.message.toString())
                        }
                    }
                }

                btPlayTrailer.setOnClickListener {
                    val dialog = BottomSheetDialog(this@DetailMovieActivity)
                    val sheet = layoutInflater.inflate(R.layout.trailer_bottom_sheet, findViewById(android.R.id.content), false)
                    dialog.setContentView(sheet)
                    dialog.show()

                    val trailerListView = sheet.findViewById<ListView>(R.id.lvTrailer)
                    val adapter = TrailerListAdapter(this@DetailMovieActivity, trailerItemList, trailerList)
                    trailerListView.adapter = adapter
                }

                /*btFavorite.setOnClickListener {
                    if (detailMovieViewModel._isFav.value == true){
                        detailMovieViewModel.deleteFromFav(movieData)
                    } else {
                        detailMovieViewModel.insertMovieFavorite(movieData)
                    }
                }*/
            }
        }
    }

    private fun setUI(movie: Movie) {
        binding?.apply {
            val backdropLink = buildString {
                append(BuildConfig.BASE_IMAGE_URL)
                append(movie.backdropPath)
            }

            val posterLink = buildString {
                append(BuildConfig.BASE_POSTER_URL)
                append(movie.poster)
            }

            if (movie.backdropPath.isNullOrEmpty()){

                val height = 236 * this@DetailMovieActivity.resources.displayMetrics.density
                ivMovieBackdrop.layoutParams.height = height.toInt()

                Glide.with(this@DetailMovieActivity)
                    .load(R.drawable.baseline_broken_image_24)
                    .into(ivMovieBackdrop)
            } else {
                Glide.with(this@DetailMovieActivity)
                    .load(backdropLink)
                    .transform(CenterCrop(), BlurTransformation(20))
                    .into(ivMovieBackdrop)
            }

            Glide.with(this@DetailMovieActivity)
                .load(posterLink)
                .into(ivMoviePoster)

            tvMovieTitle.text = movie.title
            tvOverviewContent.text = movie.overview

            val rating = movie.voteAverage?.times(10)?.toInt()
            when(rating){
                in 0..29 -> {
                    piRatingBar.setIndicatorColor(ContextCompat.getColor(this@DetailMovieActivity, R.color.badRate))
                    piRatingBar.trackColor = ContextCompat.getColor(this@DetailMovieActivity, R.color.badRateTrack)
                    piRatingBar.progress = rating ?: 0
                }
                in 30..69 -> {
                    piRatingBar.setIndicatorColor(ContextCompat.getColor(this@DetailMovieActivity, R.color.normalRate))
                    piRatingBar.trackColor = ContextCompat.getColor(this@DetailMovieActivity, R.color.normalRateTrack)
                    piRatingBar.progress = rating ?: 0
                }
                in 70..100 -> {
                    piRatingBar.setIndicatorColor(ContextCompat.getColor(this@DetailMovieActivity, R.color.goodRate))
                    piRatingBar.trackColor = ContextCompat.getColor(this@DetailMovieActivity, R.color.goodRateTrack)
                    piRatingBar.progress = rating ?: 0
                }
            }
            tvRatingPercentage.text = rating.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        DataHolder.movie = null
    }
}