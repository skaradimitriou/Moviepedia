package com.stathis.moviepedia.recyclerviews

import android.view.View
import com.stathis.moviepedia.models.Movies

interface ItemClickListener: View.OnClickListener {
    fun onItemClick(movies:Movies)
}