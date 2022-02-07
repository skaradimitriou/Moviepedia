package com.stathis.moviepedia.listeners

import com.stathis.moviepedia.models.cast.Cast

interface CastCallback {
    fun onCastClick(cast: Cast)
}