package com.stathis.moviepedia.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.FavoriteTvSeries

class FavoriteTvSeriesAdapter(
    val favoriteTvSeries: MutableList<FavoriteTvSeries>,
    private val listener: FavoriteClickListener
) : RecyclerView.Adapter<FavoriteTvSeriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTvSeriesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.popular_item_row, parent, false)
        return FavoriteTvSeriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favoriteTvSeries.size
    }

    override fun onBindViewHolder(holder: FavoriteTvSeriesViewHolder, position: Int) {
        val currentItem = favoriteTvSeries[position]
        holder.bind(currentItem,listener)
    }
}