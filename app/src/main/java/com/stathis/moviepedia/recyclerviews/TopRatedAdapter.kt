package com.stathis.moviepedia.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.Movies

class TopRatedAdapter(val popularMovies: MutableList<Movies>,val listener:ItemClickListener) :
    RecyclerView.Adapter<TopRatedViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.top_rated_item_row, parent, false)
        return TopRatedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return popularMovies.size
    }

    override fun onBindViewHolder(holder: TopRatedViewHolder, position: Int) {
        val currentItem = popularMovies[position]
        holder.bind(currentItem,listener)
    }
}