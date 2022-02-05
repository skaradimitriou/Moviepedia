package com.stathis.moviepedia.abstraction

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.stathis.moviepedia.models.LocalModel

class DiffUtilClassV2<T:LocalModel> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.equals(newItem)
    }
}