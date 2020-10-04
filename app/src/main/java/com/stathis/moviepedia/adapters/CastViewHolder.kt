package com.stathis.moviepedia.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.cast.Cast
import de.hdodenhof.circleimageview.CircleImageView

class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val castImg: CircleImageView = itemView.findViewById(R.id.castImg)
    val castName: TextView = itemView.findViewById(R.id.castName)

    fun present(cast: Cast) {

        if(cast.profile_path.isNullOrBlank()){
            castImg.setImageResource(R.drawable.profile_img_placeholder)
        } else {
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500" + cast.profile_path)
                .into(castImg)
        }

        castName.text = cast.name
    }

}