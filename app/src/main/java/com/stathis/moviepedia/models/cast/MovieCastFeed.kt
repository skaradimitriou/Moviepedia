package com.stathis.moviepedia.models.cast

import com.stathis.moviepedia.models.LocalModel

class MovieCastFeed (
    val id:Int,
    val cast:List<Cast>,
    val crew:List<Crew>
) : LocalModel