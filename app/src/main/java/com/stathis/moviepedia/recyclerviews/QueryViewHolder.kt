package com.stathis.moviepedia.recyclerviews

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.search.Query

class QueryViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {

    val name: TextView = itemView.findViewById(R.id.queryName)

    fun bind(query: Query,listener: SearchItemClickListener){
        name.text = query.queryName

        itemView.setOnClickListener{
            listener.onQueryClick(query)
        }
    }
}