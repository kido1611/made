package id.kido1611.dicoding.moviecatalogue3.db

import androidx.room.*
import id.kido1611.dicoding.moviecatalogue3.model.Movie

@Dao
interface MovieDAO {
    @Query("select * from movie where id=:id")
    fun findById(id: Int): Movie?

    @Query("select * from movie")
    fun getAllMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Delete
    fun deleteMovie(movie: Movie)

    @Query("delete from movie where id=:id")
    fun deleteMovieById(id: Int)
}