package com.stathis.moviepedia.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.Movies

class UpcomingMoviesAdapter(val upcomingMovies: MutableList<Movies>) :
    RecyclerView.Adapter<UpcomingMoviesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingMoviesViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.upcoming_movies_item_row, parent, false)
        return UpcomingMoviesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: UpcomingMoviesViewHolder, position: Int) {
        val currentItem = upcomingMovies[position]
        holder.bind(currentItem)
    }
}