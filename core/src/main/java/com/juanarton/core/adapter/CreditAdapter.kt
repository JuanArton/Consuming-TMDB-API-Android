package com.juanarton.core.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.juanarton.core.BuildConfig
import com.juanarton.core.R
import com.juanarton.core.data.domain.model.Credit
import com.juanarton.core.databinding.CreditItemViewBinding

class CreditAdapter(
    private val onClick: (Credit) -> Unit,
    private val creditList: ArrayList<Credit>
) : RecyclerView.Adapter<CreditAdapter.ViewHolder>() {

    fun setData(items: List<Credit>?) {
        creditList.apply {
            clear()
            items?.let { addAll(it) }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CreditAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.credit_item_view, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(creditList[position])

    override fun getItemCount(): Int = creditList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = CreditItemViewBinding.bind(itemView)

        fun bind(credit: Credit){
            with(itemView) {
                val imageUrl = buildString {
                    append(BuildConfig.BASE_CREDIT_PHOTO_URL)
                    append(credit.profilePath)
                }
                Log.d("creditData", imageUrl)

                binding.apply {
                    Glide.with(context)
                        .load(imageUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .fitCenter()
                        .into(ivCreditPhoto)

                    tvCreditName.text = credit.name
                    tvCharacterName.text = credit.character
                }
            }

            itemView.setOnClickListener {
                onClick(credit)
            }
        }
    }

}