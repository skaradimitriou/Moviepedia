package com.stathis.moviepedia.adapters.airing

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.listeners.old.ItemClickListener
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.models.series.TvSeries
import kotlinx.android.synthetic.main.top_rated_item_row.view.*
import kotlin.math.roundToInt

class AiringTvSeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(localModel: LocalModel, listener: ItemClickListener) {
        when(localModel){
            is TvSeries -> {
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

                if (localModel.name.isNullOrBlank()) {
                    itemView.movie_title.text = localModel.original_name
                } else {
                    itemView.movie_title.text = localModel.name
                }

                itemView.test_progressbar.progress = (localModel.vote_average * 10).roundToInt()
                itemView.ratingTxt.text = localModel.vote_average.toString()

                itemView.setOnClickListener {
                    listener.onTvSeriesClick(localModel)
                }
            }
        }
    }
}