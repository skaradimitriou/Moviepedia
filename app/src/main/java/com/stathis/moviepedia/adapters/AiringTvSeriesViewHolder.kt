package com.stathis.moviepedia.adapters

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.TvSeries
import kotlin.math.roundToInt

class AiringTvSeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val movieName: TextView = itemView.findViewById(R.id.movie_title)
    val test_progressbar:ProgressBar = itemView.findViewById(R.id.test_progressbar)
    val movieImg: ImageView = itemView.findViewById(R.id.movie_img)
    val ratingTxt: TextView = itemView.findViewById(R.id.ratingTxt)

    fun bind(tvSeriesFeed: TvSeries,listener:ItemClickListener){
        if(tvSeriesFeed.backdrop_path.isNullOrBlank()){
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500" + tvSeriesFeed.poster_path)
                .into(movieImg)
        }else {
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500" + tvSeriesFeed.backdrop_path)
                .into(movieImg)
        }

        if(tvSeriesFeed.name.isNullOrBlank()){
            movieName.text = tvSeriesFeed.original_name
        } else {
            movieName.text = tvSeriesFeed.name
        }

        test_progressbar.progress = (tvSeriesFeed.vote_average*10).roundToInt()
        ratingTxt.text = tvSeriesFeed.vote_average.toString()

        itemView.setOnClickListener{
            listener.onTvSeriesClick(tvSeriesFeed)
        }
    }
}