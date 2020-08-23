package com.stathis.moviepedia.recyclerviews


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.Movies

class ListAdapter(val listener: ItemClickListener) :
    ListAdapter<Movies, PopularMoviesViewHolder>(DiffUtilClass<Movies>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.popular_item_row, parent, false)
        return PopularMoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: PopularMoviesViewHolder, position: Int) {
        val movies = getItem(position)
        holder.bind(movies, listener)
    }
}