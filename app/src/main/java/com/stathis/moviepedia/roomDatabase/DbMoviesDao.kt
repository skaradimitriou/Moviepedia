package com.stathis.moviepedia.roomDatabase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DbMoviesDao {

    @Query("SELECT * FROM movies")
    fun readAllData(): LiveData<List<DbMovies>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovie(dbMovies: DbMovies)

    @Update
    fun updateMovie(dbMovies: DbMovies)

    @Delete
    fun deleteMovies(dbMovies: DbMovies)
}