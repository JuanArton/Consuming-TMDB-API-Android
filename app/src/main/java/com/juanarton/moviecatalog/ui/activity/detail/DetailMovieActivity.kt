package com.juanarton.moviecatalog.ui.activity.detail

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.juanarton.core.BuildConfig
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.data.source.remote.Resource
import com.juanarton.moviecatalog.R
import com.juanarton.moviecatalog.databinding.ActivityDetailMovieBinding
import com.juanarton.moviecatalog.ui.fragments.player.PlayerFragment
import com.juanarton.moviecatalog.utils.DataHolder
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

        val rowList = layoutInflater.inflate(R.layout.trailer_list_pop_up_dialog, null)
        val trailerListView = rowList.findViewById<ListView>(R.id.listTrailer)

        val dialog = AlertDialog.Builder(this).setView(rowList).create()

        movieData?.let {movie ->
            detailMovieViewModel.setProperty(movie)
            detailMovieViewModel.checkFavorite()

            setUI(movie)

            binding?.apply {

                btPlay.isVisible = false
                tvWatchTrailer.isVisible = false

                detailMovieViewModel.isFav.observe(this@DetailMovieActivity){ favStat ->
                    when (favStat) {
                        false -> {
                            btFavorite.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@DetailMovieActivity, R.drawable.baseline_favorite_border_24
                                )
                            )
                            detailMovieViewModel.setFav(favStat)
                        }
                        true -> {
                            btFavorite.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@DetailMovieActivity, R.drawable.baseline_favorite_24
                                )
                            )
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
                                        btPlay.isVisible = true
                                        tvWatchTrailer.isVisible = true
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
                                tvWatchTrailer.isVisible = false
                                lottieTrailerLoading.isVisible = false
                                btPlay.isVisible = false
                            }
                        is Resource.Loading -> Log.d("trailerData", "Loading")
                        is Resource.Error -> {
                            if (trailer.message.isNullOrEmpty()){
                                tvLoading.text = resources.getString(R.string.noTrailer)
                            } else {
                                tvLoading.text = resources.getString(R.string.failedFetchData)
                            }
                            tvWatchTrailer.isVisible = false
                            lottieTrailerLoading.isVisible = false
                            btPlay.isVisible = false
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

                btPlay.setOnClickListener {
                    buildDialogBox(trailerItemList, trailerListView, dialog)
                }

                trailerListView.setOnItemClickListener{ _, _, position, _ ->
                    val playerFragment = PlayerFragment()
                    val mBundle = Bundle()
                    mBundle.putString("videoID", trailerList[position].key)
                    playerFragment.arguments = mBundle

                    supportFragmentManager
                        .beginTransaction()
                        .replace(android.R.id.content, playerFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()

                    dialog.hide()
                }
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
                btPlay.isVisible = false
                tvWatchTrailer.text = resources.getString(R.string.noTrailer)

                val height = 236 * this@DetailMovieActivity.resources.displayMetrics.density
                ivMovieBackdrop.layoutParams.height = height.toInt()

                Glide.with(this@DetailMovieActivity)
                    .load(R.drawable.baseline_broken_image_24)
                    .into(ivMovieBackdrop)
            } else {
                Glide.with(this@DetailMovieActivity)
                    .load(backdropLink)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            ivMovieBackdrop.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                            ivMovieBackdrop.requestLayout()
                            return false
                        }
                    })
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
    private fun buildDialogBox(
        trailerItemList: List<String>,
        trailerListView: ListView,
        dialog: Dialog
    ){
        val mAdapter = ArrayAdapter(this, R.layout.trailer_item_view, R.id.text_view, trailerItemList)
        trailerListView.adapter = mAdapter

        dialog.show()

        val width = 350 * this.resources.displayMetrics.density
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = width.toInt()
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
    }

    override fun onDestroy() {
        super.onDestroy()

        DataHolder.movie = null
    }
}