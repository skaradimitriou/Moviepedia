package com.stathis.moviepedia.recyclerviews

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.Movies
import kotlin.math.roundToInt

class TopRatedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    val movieName: TextView = itemView.findViewById(R.id.movie_title)
    val movieGenre: TextView = itemView.findViewById(R.id.movie_genre)
    val movieImg: ImageView = itemView.findViewById(R.id.movie_img)
    val movieRating: TextView = itemView.findViewById(R.id.ratingTxt)
    val position:TextView = itemView.findViewById(R.id.position)
    val test_progressbar:ProgressBar = itemView.findViewById(R.id.test_progressbar)

    fun bind(movies: Movies,listener:ItemClickListener) {

        if(movies.backdrop_path.isNullOrBlank()){
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500" + movies.poster_path)
                .into(movieImg)
        }else {
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500" + movies.backdrop_path)
                .into(movieImg)
        }

        if(movies.title.isNullOrBlank()){
            movieName.text = movies.name
        } else {
            movieName.text = movies.title
        }

        movieGenre.text = movies.media_type

        test_progressbar.progress = (movies.vote_average*10).roundToInt()
        movieRating.text = movies.vote_average.toString()
        position.text = (adapterPosition+1).toString()

        itemView.setOnClickListener{
            listener.onItemClick(movies)
        }

    }
}