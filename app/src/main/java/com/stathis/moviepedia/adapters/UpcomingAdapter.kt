package com.stathis.moviepedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.listeners.ItemClickListener
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.TvSeries

class UpcomingAdapter(val listener: ItemClickListener) :
    ListAdapter<LocalModel, RecyclerView.ViewHolder>(DiffUtilClass<LocalModel>()) {

//    val MOVIES = 1
////    val TVSERIES = 2
////    val EMPTY_MODEL = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.upcoming_movies_item_row -> {
                UpcomingMoviesViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.upcoming_movies_item_row, parent, false)
                )
            }
//            TVSERIES -> {
//                FeaturedTvSeriesViewHolder(
//                    LayoutInflater.from(parent.context)
//                        .inflate(R.layout.upcoming_movies_item_row, parent, false)
//                )
//            }
            R.layout.holder_shimmer_upcoming -> {
                ShimmerViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.holder_shimmer_upcoming, parent, false)
                )
            }
            else -> UpcomingMoviesViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.upcoming_movies_item_row, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UpcomingMoviesViewHolder -> holder.bind(getItem(position), listener)
            is ShimmerViewHolder -> holder.present(getItem(position))
        }
//
//
//        val currentItem = getItem(position)
//        if (currentItem is Movies) {
//            (holder as UpcomingMoviesViewHolder).bind(currentItem, listener)
//        } else if (currentItem is EmptyModel) {
//            (holder as ShimmerViewHolder).present(currentItem)
//        } else if (currentItem is TvSeries) {
//            (holder as FeaturedTvSeriesViewHolder).present(currentItem, listener)
//        } else {
//            (holder as FeaturedTvSeriesViewHolder).present(currentItem as TvSeries, listener)
//        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Movies -> {
                R.layout.upcoming_movies_item_row
            }
            is TvSeries -> {
                R.layout.upcoming_movies_item_row
            }
            is EmptyModel -> {
                R.layout.holder_shimmer_upcoming
            }
            else -> R.layout.upcoming_movies_item_row
        }
    }
}