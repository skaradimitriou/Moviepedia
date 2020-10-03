package com.stathis.moviepedia.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.TvSeries

class TopRatedAdapter(val listener: ItemClickListener) :
    ListAdapter<Any, RecyclerView.ViewHolder>(DiffUtilClass<Any>()) {

    val MOVIES = 1
    val TVSERIES = 2
    val EMPTY_MODEL = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when {
            viewType == MOVIES -> {
                TopRatedViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.top_rated_item_row, parent, false)
                )
            }
            viewType == TVSERIES -> {
                AiringTvSeriesViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.popular_item_row, parent, false))
            }

            viewType == EMPTY_MODEL -> {
                ShimmerViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.holder_shimmer_top_rated, parent, false))
            }
            else -> AiringTvSeriesViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.popular_item_row, parent, false)
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        when {
            currentItem is Movies -> {
                (holder as TopRatedViewHolder).bind(currentItem, listener)
            }
            currentItem is TvSeries -> {
                (holder as AiringTvSeriesViewHolder).bind(currentItem, listener)
            }
            currentItem is EmptyModel -> {
                (holder as ShimmerViewHolder).present(currentItem)
            }
            else -> {
                (holder as AiringTvSeriesViewHolder).bind(currentItem as TvSeries, listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)
        return when {
            currentItem is Movies -> {
                MOVIES
            }
            currentItem is TvSeries -> {
                TVSERIES
            }
            currentItem is EmptyModel -> {
                EMPTY_MODEL
            }
            else -> {
                4
            }
        }
    }
}