package com.stathis.moviepedia.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.stathis.moviepedia.models.Movies

@Database(entities = [DbMovies::class], version = 1, exportSchema = false)
abstract class DbMoviesDatabase : RoomDatabase() {

    abstract fun dbMoviesDao(): DbMoviesDao

    companion object {
        @Volatile
        private var INSTANCE: DbMoviesDatabase? = null

        fun getDatabase(context: Context): DbMoviesDatabase{
            val tempInstance = INSTANCE
            if(tempInstance !=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                DbMoviesDatabase::class.java,"dbmovies_database").allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }

}