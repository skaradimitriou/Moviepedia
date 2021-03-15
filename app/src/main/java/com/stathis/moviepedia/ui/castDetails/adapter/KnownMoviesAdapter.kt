package com.stathis.moviepedia.ui.castDetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.stathis.moviepedia.R
import com.stathis.moviepedia.adapters.DiffUtilClass
import com.stathis.moviepedia.models.LocalModel

class KnownMoviesAdapter : ListAdapter<LocalModel, KnownMoviesViewHolder>(DiffUtilClass<LocalModel>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnownMoviesViewHolder {
        return KnownMoviesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.popular_item_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: KnownMoviesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}