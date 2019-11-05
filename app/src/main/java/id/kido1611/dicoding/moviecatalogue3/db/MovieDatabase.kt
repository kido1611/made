package id.kido1611.dicoding.moviecatalogue3.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.kido1611.dicoding.moviecatalogue3.model.Movie
import id.kido1611.dicoding.moviecatalogue3.model.TV

@Database(entities = arrayOf(Movie::class, TV::class), version = 2, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDAO(): MovieDAO
    abstract fun tvDAO(): TVDAO

    companion object {

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java,
                        "moviecatalogue.db"
                    ).build()
                    INSTANCE = instance
                }
            }
            return INSTANCE
        }
    }
}