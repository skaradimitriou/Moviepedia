package com.stathis.moviepedia.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.MovieGenres

class GenresAdapter(val movieGenres: MutableList<MovieGenres>, private val listener:GenresClickListener) : RecyclerView.Adapter<GenresViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.genres_item_row, parent, false)
        return GenresViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieGenres.size
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        val currentItem = movieGenres[position]
        holder.bind(currentItem,listener)
    }
}