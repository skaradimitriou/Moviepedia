package com.stathis.moviepedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.models.MovieGenres

class GenresAdapter(val listener: GenresClickListener) :
    androidx.recyclerview.widget.ListAdapter<Any, RecyclerView.ViewHolder>(DiffUtilClass<Any>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            R.layout.genres_item_row -> {
                return GenresViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
                )
            }
            R.layout.holder_shimmer_genres -> {
                return ShimmerViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
                )
            }
            else -> {
                return GenresViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItem(position)){
            is MovieGenres -> {
                (holder as GenresViewHolder).bind(getItem(position) as MovieGenres, listener)
            }
            is EmptyModel -> {
                (holder as ShimmerViewHolder).present(getItem(position) as EmptyModel)
            }
            else -> {}
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is MovieGenres -> {
                R.layout.genres_item_row
            }
            is EmptyModel -> {
                R.layout.holder_shimmer_genres
            }
            else -> {
                R.layout.genres_item_row
            }
        }

}