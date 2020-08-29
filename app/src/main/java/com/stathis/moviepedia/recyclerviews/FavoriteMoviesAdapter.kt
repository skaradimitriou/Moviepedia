package com.stathis.moviepedia.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.Movies

class FavoriteMoviesAdapter(
    val favoriteMovies: MutableList<FavoriteMovies>?,
    private val listener: FavoriteClickListener
) : RecyclerView.Adapter<FavoriteMoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMoviesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.popular_item_row, parent, false)
        return FavoriteMoviesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favoriteMovies!!.size
    }

    override fun onBindViewHolder(holder: FavoriteMoviesViewHolder, position: Int) {
        val currentItem = favoriteMovies!![position]
        holder.bind(currentItem, listener)
    }


}