package com.stathis.moviepedia.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.listeners.old.GenresClickListener
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.models.MovieGenres
import com.stathis.moviepedia.util.ColorHelper
import kotlinx.android.synthetic.main.genres_item_row.view.*

class GenresViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun present(localModel : LocalModel, listener: GenresClickListener) {
        when(localModel){
            is MovieGenres -> {
                itemView.genres.text = localModel.name

                ColorHelper.getAllColors().forEach {
                    if(localModel.name == it.name) {
                        itemView.genres.backgroundTintList = ColorStateList.valueOf(Color.parseColor(it.color))
                    }
                }

                itemView.setOnClickListener{
                    listener.onGenreClick(localModel)
                }
            }
        }
    }

}