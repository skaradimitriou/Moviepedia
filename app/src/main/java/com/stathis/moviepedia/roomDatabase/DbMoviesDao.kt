package com.stathis.moviepedia.roomDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DbMoviesDao {

    @Query("SELECT * FROM movies")
    fun readAllData(): LiveData<List<DbMovies>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDbMovie(dbMovies: DbMovies)
}