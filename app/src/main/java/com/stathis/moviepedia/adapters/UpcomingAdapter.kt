package com.stathis.moviepedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.listeners.ItemClickListener
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.TvSeries

class UpcomingAdapter(val listener: ItemClickListener) :
    ListAdapter<Any, RecyclerView.ViewHolder>(DiffUtilClass<Any>()) {

    val MOVIES = 1
    val TVSERIES = 2
    val EMPTY_MODEL = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MOVIES -> {
                UpcomingMoviesViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.upcoming_movies_item_row, parent, false)
                )
            }
            TVSERIES -> {
                FeaturedTvSeriesViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.upcoming_movies_item_row, parent, false)
                )
            }
            EMPTY_MODEL -> {
                ShimmerViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.holder_shimmer_upcoming, parent, false))
            }
            else -> UpcomingMoviesViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.upcoming_movies_item_row, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem is Movies) {
            (holder as UpcomingMoviesViewHolder).bind(currentItem, listener)
        } else if (currentItem is EmptyModel){
            (holder as ShimmerViewHolder).present(currentItem)
        } else if (currentItem is TvSeries){
            (holder as FeaturedTvSeriesViewHolder).present(currentItem, listener)
        } else {
            (holder as FeaturedTvSeriesViewHolder).present(currentItem as TvSeries, listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)
        return when (currentItem) {
            is Movies -> {
                MOVIES
            }
            is TvSeries -> {
                TVSERIES
            }
            is EmptyModel -> {
                EMPTY_MODEL
            }
            else -> {
                4
            }
        }
    }
}