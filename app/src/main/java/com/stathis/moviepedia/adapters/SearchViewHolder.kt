package com.stathis.moviepedia.adapters

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItem
import kotlin.math.roundToInt

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val searchImg: ImageView = itemView.findViewById(R.id.searchImg)
    val searchName: TextView = itemView.findViewById(R.id.searchName)
    val searchType: TextView = itemView.findViewById(R.id.searchType)
    val ratingTxt:TextView = itemView.findViewById(R.id.ratingTxt)
    val test_progressbar:ProgressBar = itemView.findViewById(R.id.test_progressbar)

    fun bind(searchItem: SearchItem, listener: SearchItemClickListener) {

        if (searchItem.backdrop_path.isNullOrEmpty() && searchItem.poster_path.isNullOrEmpty()){
            searchImg.setImageResource(R.drawable.default_img)
        } else if (searchItem.poster_path.isNullOrEmpty()) {
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500" + searchItem.backdrop_path)
                .into(searchImg)
        } else {
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500" + searchItem.poster_path)
                .into(searchImg)
        }

        if (searchItem.title.isNullOrEmpty()) {
            searchName.text = searchItem.name
        } else {
            searchName.text = searchItem.title
        }

        itemView.setOnClickListener{
            listener.onSearchItemClick(searchItem)
        }

        searchType.text = searchItem.media_type
        test_progressbar.progress = (searchItem.vote_average*10).roundToInt()
        ratingTxt.text = searchItem.vote_average.roundToInt().toString()
    }
}