package id.kido1611.dicoding.moviecatalogue3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.kido1611.dicoding.moviecatalogue3.model.Movie
import id.kido1611.dicoding.moviecatalogue3.model.MovieResponse
import id.kido1611.dicoding.moviecatalogue3.network.RetrofitBuilder
import id.kido1611.dicoding.moviecatalogue3.network.TheMovieDBServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListViewModel : ViewModel() {
    private val listMovies = MutableLiveData<ArrayList<Movie>>()

    internal fun setMovies() {
        val service = RetrofitBuilder.createService(TheMovieDBServices::class.java)
        val call = service.DiscoverMovie("en-US")
        call.enqueue(object: Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                val movieList = response.body() as MovieResponse
                listMovies.postValue(movieList.movieLists as ArrayList<Movie>)
            }
        })
    }

    internal fun getMovies(): LiveData<ArrayList<Movie>> {
        return listMovies
    }
}