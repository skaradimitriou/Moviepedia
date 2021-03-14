package com.stathis.moviepedia.listeners

import com.stathis.moviepedia.models.cast.Cast

interface LocalClickListener {

    fun onCastClick(cast: Cast)
}