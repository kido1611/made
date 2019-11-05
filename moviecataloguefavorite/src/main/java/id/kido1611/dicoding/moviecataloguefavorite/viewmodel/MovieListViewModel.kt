package id.kido1611.dicoding.moviecataloguefavorite.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.kido1611.dicoding.moviecataloguefavorite.model.Movie

class MovieListViewModel: ViewModel() {
    private val movieList = MutableLiveData<ArrayList<Movie>>()

    companion object{
        val CONTENT_URI: Uri = Uri.parse("content://id.kido1611.dicoding.moviecatalogue3/movie")
    }

    internal fun setData(context: Context){
        val cur = context.contentResolver.query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
        val result = ArrayList<Movie>()
        while(cur.moveToNext()){
            val data = Movie(
                id = cur.getInt(cur.getColumnIndexOrThrow("id")),
                poster = cur.getString(cur.getColumnIndexOrThrow("poster")),
                backdrop = cur.getString(cur.getColumnIndexOrThrow("backdrop")),
                title = cur.getString(cur.getColumnIndexOrThrow("title")),
                overview = cur.getString(cur.getColumnIndexOrThrow("overview")),
                releaseDate = cur.getString(cur.getColumnIndexOrThrow("releaseDate")),
                voteAverage = cur.getDouble(cur.getColumnIndexOrThrow("vote")),
                runtime = cur.getInt(cur.getColumnIndexOrThrow("runtime"))
            )
            result.add(data)
        }
        movieList.postValue(result)
    }

    internal fun getData(): LiveData<ArrayList<Movie>> = movieList
}