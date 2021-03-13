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

class TrendingAdapter(val listener: ItemClickListener) :
    ListAdapter<LocalModel, RecyclerView.ViewHolder>(DiffUtilClass<LocalModel>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.popular_item_row -> {
                PopularMoviesViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(viewType, parent, false)
                )
            }

            R.layout.holder_shimmer_popular -> {
                ShimmerViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(viewType, parent, false)
                )
            }

            else -> AiringTvSeriesViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.popular_item_row, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PopularMoviesViewHolder -> holder.bind(getItem(position),listener)
            is ShimmerViewHolder -> holder.present(getItem(position))
            else -> Unit
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Movies -> R.layout.popular_item_row
            is TvSeries -> R.layout.popular_item_row
            is EmptyModel -> R.layout.holder_shimmer_popular
            else -> R.layout.popular_item_row
        }
    }
}