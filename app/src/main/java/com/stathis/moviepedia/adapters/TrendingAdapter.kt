package com.stathis.moviepedia.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.TvSeries

class TrendingAdapter(val listener: ItemClickListener) :
    ListAdapter<Any, RecyclerView.ViewHolder>(DiffUtilClass<Any>()) {

    val MOVIES = 1
    val TVSERIES = 2
    val EMPTY_MODEL = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when {
            viewType == MOVIES -> {
                PopularMoviesViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.popular_item_row, parent, false)
                )
            }
            viewType == TVSERIES -> {
                AiringTvSeriesViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.popular_item_row, parent, false)
                )
            }

            viewType == EMPTY_MODEL -> {
                ShimmerViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.holder_shimmer_popular, parent, false)
                )
            }

            else -> AiringTvSeriesViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.popular_item_row, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        when (getItem(position)) {
            is Movies -> {
                (holder as PopularMoviesViewHolder).bind(currentItem as Movies, listener)
            }
            is TvSeries -> {
                (holder as AiringTvSeriesViewHolder).bind(currentItem as TvSeries, listener)
            }
            is EmptyModel -> {
                (holder as ShimmerViewHolder).present(currentItem as EmptyModel)
            }
            else -> {
                (holder as AiringTvSeriesViewHolder).bind(currentItem as TvSeries, listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
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