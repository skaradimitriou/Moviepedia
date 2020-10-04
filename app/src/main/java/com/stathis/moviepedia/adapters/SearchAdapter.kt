package com.stathis.moviepedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.Query
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItem

class SearchAdapter(
    private val listener: SearchItemClickListener
) : ListAdapter<Any, RecyclerView.ViewHolder>(DiffUtilClass<Any>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return when(viewType){
            R.layout.search_item_row -> {
                return SearchViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            }
            R.layout.query_item_row -> {
                return QueryViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            }
           R.layout.holder_shimmer_search_row -> {
               return ShimmerViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
           }
            else -> {
                return QueryViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItem(position)){
            is SearchItem -> {
                (holder as SearchViewHolder).bind(getItem(position) as SearchItem,listener)
            }
            is Query -> {
                (holder as QueryViewHolder).bind(getItem(position) as Query,listener)
            }
            is EmptyModel -> {
                (holder as ShimmerViewHolder).present(getItem(position) as EmptyModel)
            }
            else -> {
                4
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
       return when(getItem(position)){
            is SearchItem -> {
                R.layout.search_item_row
            }
            is Query -> {
                R.layout.query_item_row
            }
           is EmptyModel -> {
               R.layout.holder_shimmer_search_row
           }
            else -> {
                4
            }
        }
    }

}