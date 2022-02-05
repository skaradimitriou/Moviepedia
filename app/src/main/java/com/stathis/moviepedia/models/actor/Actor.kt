package com.stathis.moviepedia.models.actor

import com.stathis.moviepedia.models.LocalModel

class Actor(

    val adult: Boolean,
    val biography: String,
    val birthday: String,
    val known_for_department: String,
    val popularity: Double,
    val also_known_as : List<String>,
    val id: Int,
    val name: String,
    val place_of_birth: String,
    val profile_path: String

) : LocalModel{
    override fun equals(model: LocalModel): Boolean = false
}