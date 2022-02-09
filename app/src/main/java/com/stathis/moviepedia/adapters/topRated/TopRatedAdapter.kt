package com.stathis.moviepedia.adapters.topRated

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.adapters.DiffUtilClass
import com.stathis.moviepedia.adapters.PopularMoviesViewHolder
import com.stathis.moviepedia.adapters.ShimmerViewHolder
import com.stathis.moviepedia.adapters.airing.AiringTvSeriesViewHolder
import com.stathis.moviepedia.listeners.old.ItemClickListener
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.models.movies.Movies
import com.stathis.moviepedia.models.series.TvSeries

class TopRatedAdapter(val listener: ItemClickListener) :
    ListAdapter<LocalModel, RecyclerView.ViewHolder>(DiffUtilClass<LocalModel>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.top_rated_item_row -> {
                TopRatedViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(viewType, parent, false)
                )
            }

            R.layout.popular_item_row -> {
                PopularMoviesViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(viewType, parent, false)
                )
            }

            R.layout.holder_shimmer_top_rated -> {
                ShimmerViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(viewType, parent, false)
                )
            }
            else -> AiringTvSeriesViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(viewType, parent, false)
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TopRatedViewHolder -> holder.bind(getItem(position), listener)
            is PopularMoviesViewHolder -> holder.bind(getItem(position), listener)
            is ShimmerViewHolder -> holder.present(getItem(position))
            else -> Unit
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Movies -> R.layout.top_rated_item_row
            is TvSeries -> R.layout.popular_item_row
            is EmptyModel -> R.layout.holder_shimmer_top_rated
            else -> R.layout.popular_item_row
        }
    }
}