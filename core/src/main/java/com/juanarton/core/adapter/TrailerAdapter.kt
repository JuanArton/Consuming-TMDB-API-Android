package com.juanarton.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.juanarton.core.R
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.databinding.ItemViewBinding
import com.juanarton.core.databinding.TrailerItemViewBinding

class TrailerAdapter(
    private val onClick: (Trailer) -> Unit,
    private val trailerList: ArrayList<Trailer>
) : RecyclerView.Adapter<TrailerAdapter.ViewHolder>() {

    fun setData(items: List<Trailer>?) {
        trailerList.apply {
            clear()
            items?.let { addAll(it) }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrailerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trailer_item_view, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(trailerList[position])

    override fun getItemCount(): Int = trailerList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = TrailerItemViewBinding.bind(itemView)
        fun bind(trailer: Trailer){
            with(itemView) {
                val targetWidth = resources.displayMetrics.widthPixels
                val targetHeight = (targetWidth / 16.0 * 9.0).toInt()

                val id = trailer.key
                val thumbnailUrl = "https://img.youtube.com/vi/$id/hqdefault.jpg"

                binding.apply {
                    Glide.with(context)
                        .load(thumbnailUrl)
                        .apply(
                            RequestOptions()
                                .override(targetWidth, targetHeight)
                                .transform(CenterCrop())
                        )
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivTrailerThumbnail)

                    btPlay.setOnClickListener {
                        onClick(trailer)
                    }
                }
            }
        }
    }

}