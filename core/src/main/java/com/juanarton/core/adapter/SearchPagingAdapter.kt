package com.juanarton.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.juanarton.core.BuildConfig
import com.juanarton.core.R
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.databinding.ItemViewBinding

class SearchPagingAdapter (
    private val onClick: (Movie) -> Unit
): PagingDataAdapter<Movie, SearchPagingAdapter.GridViewHolder>(MovieComparator) {

    object MovieComparator: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchPagingAdapter.GridViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = ItemViewBinding.bind(itemView)
        fun bind(movie: Movie){

            with(itemView) {
                val imageLink = buildString {
                    append(BuildConfig.BASE_POSTER_URL)
                    append(movie.poster)
                }
                binding.apply {
                    binding.tvMovieTitle.text = movie.title

                    Glide.with(context)
                        .load(imageLink)
                        .centerCrop()
                        .into(imgPoster)
                }
            }

            itemView.setOnClickListener {
                onClick(movie)
            }
        }
    }
}