package com.stathis.moviepedia.models.cast

import com.stathis.moviepedia.models.LocalModel

class Cast (
    val cast_id:Int,
    val character :String,
    val credit_id :String,
    val id:Int,
    val name :String,
    val order:Int,
    val profile_path:String
) : LocalModel{
    override fun equals(model: LocalModel): Boolean = false
}