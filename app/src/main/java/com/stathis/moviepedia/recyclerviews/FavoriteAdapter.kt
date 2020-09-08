package com.stathis.moviepedia.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.databinding.PopularItemRowBinding
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.FavoriteTvSeries
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.TvSeries

class FavoriteAdapter(
    private val listener: FavoriteClickListener
) : ListAdapter<Any, RecyclerView.ViewHolder>(DiffUtilClass<Any>()) {

    val FAVORITE_MOVIES = 1
    val FAVORITE_TVSERIES = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == FAVORITE_MOVIES) {
            val view = PopularItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return FavoriteMoviesViewHolder(view)
//            FavoriteMoviesViewHolder(
//                LayoutInflater.from(parent.context)
//                    .inflate(R.layout.popular_item_row, parent, false)
//            )
        } else {
            return FavoriteTvSeriesViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.popular_item_row, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem is FavoriteMovies) {
            (holder as FavoriteMoviesViewHolder).bind(currentItem as FavoriteMovies,listener)
        } else {
            (holder as FavoriteTvSeriesViewHolder).bind(currentItem as FavoriteTvSeries,listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)
        return if (currentItem is FavoriteMovies) {
            FAVORITE_MOVIES
        } else {
            FAVORITE_TVSERIES
        }
    }
}