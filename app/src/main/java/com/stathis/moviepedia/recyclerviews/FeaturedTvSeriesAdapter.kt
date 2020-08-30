package com.stathis.moviepedia.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.TvSeries

class FeaturedTvSeriesAdapter(val tvSeries: MutableList<TvSeries>?, private val listener: ItemClickListener) : RecyclerView.Adapter<FeaturedTvSeriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedTvSeriesViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.upcoming_movies_item_row, parent, false)
        return FeaturedTvSeriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tvSeries!!.size
    }

    override fun onBindViewHolder(holder: FeaturedTvSeriesViewHolder, position: Int) {
        val currentItem = tvSeries!![position]
        holder.present(currentItem,listener)
    }
}