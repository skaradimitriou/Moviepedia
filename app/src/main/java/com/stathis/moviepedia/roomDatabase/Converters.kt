package com.stathis.moviepedia.roomDatabase

import android.renderscript.Type
import androidx.core.content.contentValuesOf
import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.*

class Converters {

    @TypeConverter
    fun fromList(dbMoviesGenres: List<Int>): String {
        return dbMoviesGenres.toString()
    }

    @TypeConverter
    fun intToList(value: String): List<Int>? {
        return value.split(",").filter { it.toIntOrNull() != null }
            .map { it.toInt() }
    }
}