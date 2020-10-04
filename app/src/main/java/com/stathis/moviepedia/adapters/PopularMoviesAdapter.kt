package com.stathis.moviepedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.Movies

class PopularMoviesAdapter(val popularMovies: MutableList<Movies>, private val listener: ItemClickListener) :
    RecyclerView.Adapter<PopularMoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.popular_item_row, parent, false)
        return PopularMoviesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return popularMovies.size
    }

    override fun onBindViewHolder(holder: PopularMoviesViewHolder, position: Int) {
        val currentItem = popularMovies[position]
        holder.bind(currentItem,listener)
    }
}