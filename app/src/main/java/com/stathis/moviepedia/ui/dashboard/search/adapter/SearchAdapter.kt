package com.stathis.moviepedia.ui.dashboard.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.DiffUtilClassV2
import com.stathis.moviepedia.databinding.*
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.ui.dashboard.search.models.Query
import com.stathis.moviepedia.ui.dashboard.search.models.SearchItem

class SearchAdapter(val callback : GenericCallback) : ListAdapter<LocalModel,SearchViewHolder>(DiffUtilClassV2<LocalModel>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = when(viewType){
            R.layout.holder_query_item -> HolderQueryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            R.layout.holder_search_item -> HolderSearchItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            else -> HolderEmptyLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        }

        return SearchViewHolder(view,callback)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.present(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = when(getItem(position)){
        is Query -> R.layout.holder_query_item
        is SearchItem -> R.layout.holder_search_item
        else -> R.layout.holder_empty_layout
    }
}