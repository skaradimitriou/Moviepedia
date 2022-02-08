package com.stathis.moviepedia.ui.movieDetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.DiffUtilClassV2
import com.stathis.moviepedia.adapters.NewShimmerViewHolder
import com.stathis.moviepedia.databinding.HolderCastItemRowBinding
import com.stathis.moviepedia.databinding.HolderShimmerCastItemBinding
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.models.cast.Cast

class CastAdapter(val callback: GenericCallback) : ListAdapter<LocalModel, RecyclerView.ViewHolder>(DiffUtilClassV2<LocalModel>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.holder_cast_item_row -> {
                val view = HolderCastItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                CastViewHolder(view,callback)
            }
            else -> {
                val view = HolderShimmerCastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                NewShimmerViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is CastViewHolder -> holder.present(getItem(position))
        is NewShimmerViewHolder -> holder.present(getItem(position))
        else -> Unit
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is Cast -> R.layout.holder_cast_item_row
        is EmptyModel -> R.layout.holder_shimmer_cast_item
        else -> R.layout.holder_empty_layout
    }
}