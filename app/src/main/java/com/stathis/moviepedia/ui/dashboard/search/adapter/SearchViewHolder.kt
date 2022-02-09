package com.stathis.moviepedia.ui.dashboard.search.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.BR
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.ui.dashboard.search.models.Query
import com.stathis.moviepedia.ui.dashboard.search.models.SearchItem

class SearchViewHolder(val binding: ViewDataBinding, val callback: GenericCallback) : RecyclerView.ViewHolder(binding.root) {

    fun present(data: LocalModel) = when (data) {
        is SearchItem -> bind(data, callback)
        is Query -> bind(data, callback)
        else -> Unit
    }

    private fun bind(model: LocalModel, callback: GenericCallback) {
        binding.setVariable(BR.model, model)
        binding.setVariable(BR.callback, callback)
    }
}