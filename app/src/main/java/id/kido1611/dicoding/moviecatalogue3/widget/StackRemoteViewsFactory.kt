package id.kido1611.dicoding.moviecatalogue3.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import id.kido1611.dicoding.moviecatalogue3.R
import id.kido1611.dicoding.moviecatalogue3.model.Movie

internal class StackRemoteViewsFactory(private val context: Context): RemoteViewsService.RemoteViewsFactory {
    private var movieItems = ArrayList<Movie>()

    override fun onCreate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = 0

    override fun onDataSetChanged() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hasStableIds(): Boolean = false

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.item_widget)
        val movie = movieItems[position]

        val poster = Glide.with(context)
            .asBitmap()
            .load("https://image.tmdb.org/t/p/w92" + movie.poster)
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