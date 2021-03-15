package com.stathis.moviepedia.ui.castDetails.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.models.actor.KnownMovies
import kotlinx.android.synthetic.main.popular_item_row.view.*
import kotlin.math.roundToInt

class KnownMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(localModel: LocalModel) {
        when (localModel) {
            is KnownMovies -> {
                if (localModel.backdrop_path.isNullOrBlank()) {
                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w500" + localModel.photo)
                        .placeholder(R.drawable.default_img)
                        .into(itemView.movie_img)
                } else {
                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w500" + localModel.backdrop_path)
                        .placeholder(R.drawable.default_img)
                        .into(itemView.movie_img)
                }

                itemView.movie_title.text = localModel.title

                itemView.test_progressbar.progress = (localModel.vote_average * 10).roundToInt()
                itemView.ratingTxt.text = localModel.vote_average.toString()
            }
        }
    }
}