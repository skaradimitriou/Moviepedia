package com.stathis.moviepedia.recyclerviews

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.stathis.moviepedia.models.Movies
import java.util.*

class DiffUtilClass <T:Any> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}