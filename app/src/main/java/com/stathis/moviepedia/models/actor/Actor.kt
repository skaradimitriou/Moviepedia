package com.stathis.moviepedia.models.actor

import com.stathis.moviepedia.models.LocalModel

class Actor(

    val adult : Boolean,
    val biography : String,
    val birthday : String,
    val id : Int,
    val name : String,
    val place_of_birth : String,
    val profile_path : String

) : LocalModel