package com.juanarton.moviecatalog.ui.activity.detail

import android.content.res.ColorStateList
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.juanarton.core.BuildConfig
import com.juanarton.core.adapter.TrailerAdapter
import com.juanarton.core.data.domain.model.DetailMovie
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.data.source.remote.Resource
import com.juanarton.moviecatalog.R
import com.juanarton.moviecatalog.databinding.ActivityDetailMovieBinding
import com.juanarton.moviecatalog.ui.fragments.player.PlayerFragment
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

        val dialog = BottomSheetDialog(this@DetailMovieActivity)
        val sheet = layoutInflater.inflate(R.layout.trailer_bottom_sheet, findViewById(android.R.id.content), false)

        val trailerList: MutableList<Trailer> = mutableListOf()

        movieData?.let {movie ->
            detailMovieViewModel.setProperty(movie)
            detailMovieViewModel.checkFavorite()

            detailMovieViewModel.movieDetail.observe(this) {
                when(it){
                    is Resource.Success -> {
                        it.data?.let {detailMovie ->
                            setUI(movie, detailMovie)
                        }
                    }
                    is Resource.Loading -> Log.d("detailmMovie", "Loading")
                    is Resource.Error -> {
                    }
                }
            }

            binding?.apply {

                detailMovieViewModel.isFav.observe(this@DetailMovieActivity){ favStat ->
                    when (favStat) {
                        false -> {
                            val color = ColorStateList.valueOf(
                                ContextCompat.getColor(this@DetailMovieActivity, R.color.white)
                            )
                            btFavorite.apply {
                                setIconResource(R.drawable.baseline_favorite_border_18)
                                strokeColor = color
                                setTextColor(color)
                                iconTint = color
                            }
                            detailMovieViewModel.setFav(favStat)
                        }
                        true -> {
                            val color = ColorStateList.valueOf(
                                ContextCompat.getColor(this@DetailMovieActivity, R.color.red)
                            )
                            btFavorite.apply {
                                setIconResource(R.drawable.ic_baseline_favorite_24)
                                strokeColor = color
                                setTextColor(color)
                                iconTint = color
                            }
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
                                    val listener: (Trailer) -> Unit = {
                                        val playerFragment = PlayerFragment()
                                        val mBundle = Bundle()
                                        mBundle.putString("videoID", it.key)
                                        playerFragment.arguments = mBundle

                                        dialog.dismiss()

                                        supportFragmentManager
                                            .beginTransaction()
                                            .replace(android.R.id.content, playerFragment)
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                            .commit()
                                    }

                                    rvTrailerItem.layoutManager = LinearLayoutManager(
                                        this@DetailMovieActivity,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                    val rvAdapter = TrailerAdapter(listener, arrayListOf())
                                    rvTrailerItem.adapter = rvAdapter
                                    rvAdapter.setData(trailerData)
                                    val snapHelper = PagerSnapHelper()
                                    snapHelper.attachToRecyclerView(rvTrailerItem)
                                    rvAdapter.notifyDataSetChanged()
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

                btFavorite.setOnClickListener {
                    if (detailMovieViewModel._isFav.value == true){
                        detailMovieViewModel.deleteFromFav(movieData)
                    } else {
                        detailMovieViewModel.insertMovieFavorite(movieData)
                    }
                }

                val btCloseBottomSheet = sheet.findViewById<MaterialButton>(R.id.btClose)
                btCloseBottomSheet.setOnClickListener {
                    dialog.dismiss()
                }
            }
        }
    }

    private fun setUI(movie: Movie, detailMovie: DetailMovie) {
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
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    Glide.with(this@DetailMovieActivity)
                        .load(backdropLink)
                        .transform(CenterCrop(), BlurTransformation(20))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivMovieBackdrop)
                } else {
                    Glide.with(this@DetailMovieActivity)
                        .load(backdropLink)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivMovieBackdrop)

                    ivMovieBackdrop.setRenderEffect(
                        RenderEffect.createBlurEffect(
                            20.0F, 20.0F, Shader.TileMode.CLAMP
                        )
                    )
                }
            }

            Glide.with(this@DetailMovieActivity)
                .load(posterLink)
                .into(ivMoviePoster)

            tvMovieTitle.text = movie.title
            tvOverviewContent.text = movie.overview
            tvGenres.text = detailMovie.genres
            tvRuntime.text = detailMovie.runtime
            tvReleaseDate.text = movie.releaseDate

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