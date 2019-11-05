package id.kido1611.dicoding.moviecatalogue3.db

import android.database.Cursor
import androidx.room.*
import id.kido1611.dicoding.moviecatalogue3.model.Movie

@Dao
interface MovieDAO {
    @Query("select * from movie where id=:id")
    fun findById(id: Int): Movie?

    @Query("select * from movie")
    fun getAllMovies(): List<Movie>

    @Query("select * from movie")
    fun getAllMoviesProviders(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie) : Long

    @Delete
    fun deleteMovie(movie: Movie)

    @Query("delete from movie where id=:id")
    fun deleteMovieById(id: Int)
}