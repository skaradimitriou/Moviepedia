package com.stathis.moviepedia.util

import androidx.lifecycle.MutableLiveData
import com.stathis.moviepedia.models.ColorModel

object ColorHelper {

    val color = MutableLiveData<String>()
    private var colorRepo : List<ColorModel> = listOf(
        ColorModel("Action", "#4f5fef"),
        ColorModel("Adventure", "#23B993"),
        ColorModel("Animation", "#ff0045"),
        ColorModel("Comedy", "#f86611"),
        ColorModel("Crime", "#EC5657"),
        ColorModel("Documentary", "#2D2C4E"),
        ColorModel("Drama", "#000000"),
        ColorModel("Family", "#4f5fef"),
        ColorModel("Fantasy", "#23B993"),
        ColorModel("History", "#ff0045"),
        ColorModel("Horror", "#f86611"),
        ColorModel("Music", "#EC5657"),
        ColorModel("Mystery", "#2D2C4E"),
        ColorModel("Romance", "#000000"),
        ColorModel("Science Fiction", "#4f5fef"),
        ColorModel("TV Movie", "#23B993"),
        ColorModel("Thriller", "#ff0045"),
        ColorModel("War", "#f86611"),
        ColorModel("Western", "#EC5657")
    )

    fun getBackgroundColor(genreName: String) {
        colorRepo.forEach {
            if (genreName == it.name) {
                color.value = it.color
            }
        }
    }

    fun getAllColors(): List<ColorModel> {
        return colorRepo
    }
}