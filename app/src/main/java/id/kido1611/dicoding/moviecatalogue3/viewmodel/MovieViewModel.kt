package id.kido1611.dicoding.moviecatalogue3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.kido1611.dicoding.moviecatalogue3.handler.ViewModelHandler
import id.kido1611.dicoding.moviecatalogue3.model.Movie
import id.kido1611.dicoding.moviecatalogue3.network.RetrofitBuilder
import id.kido1611.dicoding.moviecatalogue3.network.TheMovieDBServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {
    private val movie = MutableLiveData<Movie>()
    private var viewModelHandler: ViewModelHandler? = null

    internal fun setViewModelHandler(handler: ViewModelHandler){
        viewModelHandler = handler
    }

    internal fun setMovie(movieId: Int, language: String) {
        viewModelHandler?.onInit()
        val service = RetrofitBuilder.createService(TheMovieDBServices::class.java)
        val call = service.GetMovie(movieId, language)
        call.enqueue(object : Callback<Movie> {
            override fun onFailure(call: Call<Movie>, t: Throwable) {
                viewModelHandler?.onFailure(t.message.toString())
            }

            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                val item = response.body() as Movie
                movie.postValue(item)
                viewModelHandler?.onSuccess()
            }

        })
    }

    internal fun getMovie(): LiveData<Movie> {
        return movie
    }
}