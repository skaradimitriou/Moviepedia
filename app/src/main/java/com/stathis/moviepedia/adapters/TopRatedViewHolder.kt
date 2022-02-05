package com.stathis.moviepedia.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.listeners.old.ItemClickListener
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.models.Movies
import kotlinx.android.synthetic.main.top_rated_item_row.view.*
import kotlin.math.roundToInt

class TopRatedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(localModel: LocalModel, listener: ItemClickListener) {
        when(localModel){
            is Movies -> {
                if (localModel.backdrop_path.isNullOrBlank()) {
                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w500" + localModel.poster_path)
                        .placeholder(R.drawable.default_img)
                        .into(itemView.movie_img)
                } else {
                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w500" + localModel.backdrop_path)
                        .placeholder(R.drawable.default_img)
                        .into(itemView.movie_img)
                }

                if (localModel.title.isNullOrBlank()) {
                    itemView.movie_title.text = localModel.name
                } else {
                    itemView.movie_title.text = localModel.title
                }

                itemView.movie_genre.text = localModel.media_type

                itemView.test_progressbar.progress = (localModel.vote_average * 10).roundToInt()
                itemView.ratingTxt.text = localModel.vote_average.toString()
                itemView.position.text = (adapterPosition + 1).toString()

                itemView.setOnClickListener {
                    listener.onItemClick(localModel)
                }
            }
        }
    }
}