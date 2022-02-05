package com.stathis.moviepedia.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.listeners.old.SearchItemClickListener
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItem
import kotlinx.android.synthetic.main.search_item_row.view.*
import kotlin.math.roundToInt

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(localModel: LocalModel, listener: SearchItemClickListener) {
        when (localModel) {
            is SearchItem -> {

                if (localModel.backdrop_path.isNullOrEmpty() && localModel.poster_path.isNullOrEmpty()) {
                    itemView.searchImg.setImageResource(R.drawable.default_img)
                } else if (localModel.poster_path.isNullOrEmpty()) {
                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w500" + localModel.backdrop_path)
                        .placeholder(R.drawable.default_img)
                        .into(itemView.searchImg)
                } else {
                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w500" + localModel.poster_path)
                        .placeholder(R.drawable.default_img)
                        .into(itemView.searchImg)
                }

                if (localModel.title.isNullOrEmpty()) {
                    itemView.searchName.text = localModel.name
                } else {
                    itemView.searchName.text = localModel.title
                }

                itemView.setOnClickListener {
                    listener.onSearchItemClick(localModel)
                }

                itemView.searchType.text = localModel.media_type
                itemView.test_progressbar.progress = (localModel.vote_average * 10).roundToInt()
                itemView.ratingTxt.text = localModel.vote_average.roundToInt().toString()
            }
        }
    }
}