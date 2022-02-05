package com.stathis.moviepedia.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.listeners.old.SearchItemClickListener
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.Query
import kotlinx.android.synthetic.main.query_item_row.view.*

class QueryViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {

    fun present(localModel: LocalModel, listener: SearchItemClickListener){
        when(localModel){
            is Query -> {
                itemView.queryName.text = localModel.queryName

                itemView.setOnClickListener{
                    listener.onQueryClick(localModel)
                }
            }
        }
    }
}