package com.stathis.moviepedia.recyclerviews

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.stathis.moviepedia.models.Movies
import java.util.*

class DiffUtilClass <movies:Movies> : DiffUtil.ItemCallback<movies>() {

    override fun areItemsTheSame(oldItem: movies, newItem: movies): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: movies, newItem: movies): Boolean {
        return oldItem == newItem
    }
}