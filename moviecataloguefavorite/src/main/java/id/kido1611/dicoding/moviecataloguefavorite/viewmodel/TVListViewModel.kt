package id.kido1611.dicoding.moviecataloguefavorite.viewmodel

import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.kido1611.dicoding.moviecataloguefavorite.model.TV

class TVListViewModel: ViewModel() {
    private val movieList = MutableLiveData<ArrayList<TV>>()

    companion object{
        val CONTENT_URI: Uri = Uri.parse("content://id.kido1611.dicoding.moviecatalogue3/tv")
    }

    internal fun setData(context: Context){
        val cur: Cursor? = context.contentResolver.query(
            CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val result = ArrayList<TV>()
        while(cur?.moveToNext() as Boolean){
            val data = TV(
                id = cur.getInt(cur.getColumnIndexOrThrow("id")),
                poster = cur.getString(cur.getColumnIndexOrThrow("poster")),
                backdrop = cur.getString(cur.getColumnIndexOrThrow("backdrop")),
                title = cur.getString(cur.getColumnIndexOrThrow("title")),
                overview = cur.getString(cur.getColumnIndexOrThrow("overview")),
                releaseDate = cur.getString(cur.getColumnIndexOrThrow("firstairdate")),
                voteAverage = cur.getDouble(cur.getColumnIndexOrThrow("vote")),
                runtime = null
            )
            result.add(data)
        }
        movieList.postValue(result)
    }

    internal fun getData(): LiveData<ArrayList<TV>> = movieList
}