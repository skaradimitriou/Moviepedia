package com.stathis.moviepedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.stathis.moviepedia.R
import com.stathis.moviepedia.listeners.old.ItemClickListener
import com.stathis.moviepedia.models.series.TvSeries

class AiringTvSeriesAdapter(private val listener: ItemClickListener) :
    ListAdapter<Any, PopularMoviesViewHolder>(DiffUtilClass<Any>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesViewHolder {
        return PopularMoviesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.popular_item_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PopularMoviesViewHolder, position: Int) {
        holder.bind(getItem(position) as TvSeries, listener)
    }
}