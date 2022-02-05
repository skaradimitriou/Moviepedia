package com.stathis.moviepedia.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.listeners.old.LocalClickListener
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.models.cast.Cast
import kotlinx.android.synthetic.main.cast_item_row.view.*

class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun present(localModel: LocalModel, callback: LocalClickListener) {
        when (localModel) {
            is Cast -> {
                if (localModel.profile_path.isNullOrBlank()) {
                    itemView.castImg.setImageResource(R.drawable.profile_img_placeholder)
                } else {
                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w500" + localModel.profile_path)
                        .placeholder(R.drawable.default_img)
                        .into(itemView.castImg)
                }

                itemView.castName.text = localModel.name

                itemView.setOnClickListener { callback.onCastClick(localModel) }
            }
        }
    }
}