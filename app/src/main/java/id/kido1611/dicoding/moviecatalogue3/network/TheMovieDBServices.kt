package id.kido1611.dicoding.moviecatalogue3.network

import id.kido1611.dicoding.moviecatalogue3.model.Movie
import id.kido1611.dicoding.moviecatalogue3.model.MovieResponse
import id.kido1611.dicoding.moviecatalogue3.model.TV
import id.kido1611.dicoding.moviecatalogue3.model.TVResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBServices {
    @GET("discover/movie")
    fun DiscoverMovie(@Query("language") language: String): Call<MovieResponse>

    @GET("discover/tv")
    fun DiscoverTV(@Query("language") language: String): Call<TVResponse>

    @GET("movie/{movieid}")
    fun GetMovie(@Path("movieid") id: Int, @Query("language") language: String): Call<Movie>

    @GET("tv/{tvid}")
    fun GetTV(@Path("tvid") id: Int, @Query("language") language: String): Call<TV>
}