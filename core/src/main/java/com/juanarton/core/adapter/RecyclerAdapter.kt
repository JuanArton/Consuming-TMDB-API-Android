package com.juanarton.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.juanarton.core.BuildConfig
import com.juanarton.core.R
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.databinding.ItemViewBinding

class RecyclerAdapter(private val onClick: (Movie) -> Unit, private val movieList: List<Movie>):
    RecyclerView.Adapter<RecyclerAdapter.GridViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.GridViewHolder, position: Int) {
        val movieData = movieList[position]
        holder.bind(movieData)
    }

    override fun getItemCount(): Int = movieList.size

    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = ItemViewBinding.bind(itemView)
        fun bind(movie: Movie){
            with(itemView) {
                val imageLink = buildString {
                    append(BuildConfig.BASE_IMAGE_URL)
                    append(movie.backdrop_path)
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

    interface OnItemClickCallback {
        fun onItemClicked(data: Movie)
    }
}