package id.kido1611.dicoding.moviecatalogue3.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import id.kido1611.dicoding.moviecatalogue3.R
import id.kido1611.dicoding.moviecatalogue3.db.FavoriteMovieProvider
import id.kido1611.dicoding.moviecatalogue3.db.MovieDatabase
import id.kido1611.dicoding.moviecatalogue3.model.Movie

internal class StackRemoteViewsFactory(private val context: Context): RemoteViewsService.RemoteViewsFactory {
    private var movieItems = ArrayList<Movie>()
    private var cursor: Cursor? = null
    private lateinit var movieDatabase: MovieDatabase

    override fun onCreate() {
        movieDatabase = MovieDatabase.getInstance(context.applicationContext)!!
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = 0

    override fun onDataSetChanged() {
        if(cursor != null){
            cursor?.close()
        }

        movieItems.clear()
        val identityToken = Binder.clearCallingIdentity()
        cursor = context.contentResolver.query(
            FavoriteMovieProvider.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val result = convertCursorToList(cursor!!)
        movieItems.addAll(result)

        Binder.restoreCallingIdentity(identityToken)
    }

    private fun convertCursorToList(cur: Cursor) : ArrayList<Movie>{
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

        return result
    }

    override fun hasStableIds(): Boolean = false

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.item_widget)
        if(movieItems.size == 0){
            return rv
        }
        val movie = movieItems[position]

        val poster = Glide.with(context)
            .asBitmap()
            .load("https://image.tmdb.org/t/p/w185" + movie.poster)
            .submit()
            .get()

        rv.setImageViewBitmap(R.id.imageView, poster)

        val extras = bundleOf(
            FavoriteMovieWidget.EXTRA_ITEM to movie.id
        )

        val fillIntent = Intent()
        fillIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillIntent)
        return rv
    }

    override fun getCount(): Int = movieItems.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {

    }
}