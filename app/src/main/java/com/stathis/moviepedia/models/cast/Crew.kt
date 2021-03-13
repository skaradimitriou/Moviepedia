package com.stathis.moviepedia.models.cast

import com.stathis.moviepedia.models.LocalModel

class Crew (
    val credit_id:String,
    val department:String,
    val gender: Int,
    val id:Int,
    val job:String,
    val name:String,
    val profile_path:String
) : LocalModel