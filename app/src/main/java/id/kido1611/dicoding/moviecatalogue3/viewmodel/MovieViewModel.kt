package id.kido1611.dicoding.moviecatalogue3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.kido1611.dicoding.moviecatalogue3.model.Movie

class MovieViewModel : ViewModel() {
    private val movie = MutableLiveData<Movie>()

    internal fun setMovie(item: Movie) {
        movie.postValue(item)
    }

    internal fun getMovie(): LiveData<Movie> {
        return movie
    }
}