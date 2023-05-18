package com.juanarton.favorite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.juanarton.core.BuildConfig
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.favorite.R
import com.juanarton.favorite.databinding.FavoriteItemViewBinding

class FavoriteRecylerAdapter(
    private val onClick: (Movie) -> Unit
): RecyclerView.Adapter<FavoriteRecylerAdapter.ListViewHolder>() {

    private val favoriteList: MutableList<Movie> = mutableListOf()

    fun submit(favoriteList: List<Movie>) {
        this.favoriteList.clear()
        this.favoriteList.addAll(favoriteList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_item_view, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = favoriteList.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val favoriteList = this.favoriteList[position]
        holder.bind(favoriteList)
    }

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = FavoriteItemViewBinding.bind(itemView)
        fun bind(movie: Movie){
            with(itemView){
                binding.apply {
                    val imageLink = buildString {
                        append(BuildConfig.BASE_POSTER_URL)
                        append(movie.poster)
                    }

                    tvFavItemMovieTitle.text = movie.title

                    Glide.with(context)
                        .load(imageLink)
                        .centerCrop()
                        .into(ivFavItemImgPoster)

                    tvFavItemOverview.text = movie.overview

                    val rating = movie.voteAverage?.times(10)?.toInt()
                    when(rating){
                        in 0..29 -> {
                            piFavItemRatingBar.setIndicatorColor(ContextCompat.getColor(context, R.color.badRate))
                            piFavItemRatingBar.trackColor = ContextCompat.getColor(context, R.color.badRateTrack)
                            piFavItemRatingBar.progress = rating ?: 0
                        }

                        in 30..69 -> {
                            piFavItemRatingBar.setIndicatorColor(ContextCompat.getColor(context, R.color.normalRate))
                            piFavItemRatingBar.trackColor = ContextCompat.getColor(context, R.color.normalRateTrack)
                            piFavItemRatingBar.progress = rating ?: 0
                        }

                        in 70..100 ->{
                            piFavItemRatingBar.setIndicatorColor(ContextCompat.getColor(context, R.color.goodRate))
                            piFavItemRatingBar.trackColor = ContextCompat.getColor(context, R.color.goodRateTrack)
                            piFavItemRatingBar.progress = rating ?: 0
                        }
                    }
                    tvFavItemRatingPercentage.text = rating.toString()
                }

                itemView.setOnClickListener {
                    onClick(movie)
                }
            }
        }
    }
}