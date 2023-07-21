package com.juanarton.core.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.databinding.TrailerItemViewBinding

class TrailerListAdapter(
    private val onClick: (String) -> Unit,
    context: Context,
    private val title: String?,
    private val trailerItemList: List<Trailer>
) : ArrayAdapter<Trailer>(context, 0, trailerItemList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: TrailerItemViewBinding

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            binding = TrailerItemViewBinding.inflate(inflater, parent, false)
            binding.root.tag = binding
        } else {
            binding = convertView.tag as TrailerItemViewBinding
        }

        val id = trailerItemList[position].key

        val thumbnailUrl = "https://img.youtube.com/vi/$id/hqdefault.jpg"

        binding.apply {
            Glide.with(context)
                .load(thumbnailUrl)
                .into(binding.ivTrailerThumbnail)

            val tempPosition = position+1
            val title = "Trailer $tempPosition - $title"
            tvTrailerTitle.text = title

            root.setOnClickListener {
                onClick(id)
            }
        }

        return binding.root
    }
}