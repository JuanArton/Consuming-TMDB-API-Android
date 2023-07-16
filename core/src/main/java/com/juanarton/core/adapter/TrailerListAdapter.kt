package com.juanarton.core.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.databinding.TrailerItemViewBinding

class TrailerListAdapter(
    context: Context,
    private val trailerTitle: List<String>,
    private val trailerItemList: List<Trailer>
) : ArrayAdapter<String>(context, 0, trailerTitle) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: TrailerItemViewBinding

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            binding = TrailerItemViewBinding.inflate(inflater, parent, false)
            binding.root.tag = binding
        } else {
            binding = convertView.tag as TrailerItemViewBinding
        }

        val title = trailerTitle[position]
        val id = trailerItemList[position].key

        binding.tvTrailerTitle.text = title

        val thumbnailUrl = "https://img.youtube.com/vi/$id/hqdefault.jpg"

        Glide.with(context)
            .load(thumbnailUrl)
            .into(binding.ivTrailerThumbnail)

        Log.d("trailer", thumbnailUrl)

        return binding.root
    }
}